package hu.remzso.tarantulaForum.controllers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import hu.remzso.tarantulaForum.entities.Address;
import hu.remzso.tarantulaForum.entities.FileEntity;
import hu.remzso.tarantulaForum.entities.Message;
import hu.remzso.tarantulaForum.entities.Tarantula;
import hu.remzso.tarantulaForum.entities.User;
import hu.remzso.tarantulaForum.exceptions.EmailNotFoundException;
import hu.remzso.tarantulaForum.exceptions.UsernameNotFoundException;
import hu.remzso.tarantulaForum.repositories.FileEntityRepository;
import hu.remzso.tarantulaForum.repositories.MessageRepository;
import hu.remzso.tarantulaForum.repositories.TarantulaRepository;
import hu.remzso.tarantulaForum.repositories.UserRepository;
import hu.remzso.tarantulaForum.services.UserServiceImpl;

@Controller
public class TarantulaController {
	private static final String imagesPath = "\\src\\main\\resources\\static\\img\\uploadedImages\\";
	private static final String imagesPathToDB = "\\static\\img\\uploadedImages\\";
	private static final String projectLocation = "\\eclipse-workspace\\tarantulaForum";
	private static final String JPGExtension = ".jpg";
	private User userForRegistration;

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private TarantulaRepository tarantulaRepository;
	@Autowired
	private FileEntityRepository fileEntityRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MessageRepository messageRepository;

	@RequestMapping("/login")
	public String login() {

		return "actualUser";
	}

	@RequestMapping("/info")
	public String renderInfo() {

		return "/info";
	}

	@GetMapping("/actualUser")
	public String renderActualUser() {

		return "actualUser";
	}

	@GetMapping("/tarantula")
	public String renderTarantula(@RequestParam("id") Long id, Model model) {
		List<String> images = new ArrayList<>();
		for (FileEntity fileEntity : fileEntityRepository.findAllByTarantulaID(id).get()) {
			images.add(fileEntity.getPath());
		}
		Tarantula tarantula = tarantulaRepository.findById(id).get();

		String path = fileEntityRepository.findFirstByTarantulaIDOrderByIdAsc(id).get().getPath();
		path = path.replace("\\", "/");
		model.addAttribute("images", images);
		model.addAttribute("tarantula", tarantula);
		model.addAttribute("imagePath", path);
		return "tarantula";
	}

	@RequestMapping("/index")
	public String renderIndex() {

		return "index";
	}

	@RequestMapping("/register")
	public String renderRegister() {

		return "register";
	}

	@RequestMapping("/registerUser")
	public String registerUser(@ModelAttribute User user) {
		userForRegistration = user;

		return "registerAddress";
	}

	@RequestMapping("/registerAddress")
	public String registerAdress(@ModelAttribute Address address) {

		if (userForRegistration != null) {
			address.setUser(userForRegistration);
			userForRegistration.setAddresses(List.of(address));
			userServiceImpl.saveUser(userForRegistration);
			userForRegistration = null;

			return "thanks";
		}

		return "registrationError";
	}

	@RequestMapping("/tarantulas")
	public String listTarantulas(Model model) {
		List<Tarantula> tarantulas = tarantulaRepository.findAll();
		Collections.sort(tarantulas);
		model.addAttribute("tarantulas", tarantulas);
		return "tarantulas";
	}

	@RequestMapping("/uploadTarantula")
	public String renderTarantulaUlpoad(Model model) {

		return "tarantulaUpload";
	}

	@RequestMapping("/writeMessage")
	public String renderWriteMessagePage() {

		return "message";
	}
	@RequestMapping("/ownMessages")
	public String renderMessage() {

		return "ownMessages";
	}

	@RequestMapping("/sendMessage")
	public String sengMessage(@RequestParam("recipients") String recipients,
			@RequestParam("message") String messageToSend,
			@RequestParam("tittle") String tittle) {
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String username = userDetails.getUsername();
		User sender = userRepository.findByUsername(username);

		// A fentiekhez kellenek majd ellenőrzések!!!!

		//Kell egy logika ami ellenőrzi, hogy email cím avgy felhasználónév alapján küldünk üzenetet.
		
		// ha ez a metódus kivételt dob akkor arra reagálni kell kliens oldalon.
		Set<User> recipientUsers = createUseresFromUsernames(recipients);
		Message message = new Message(null, tittle,sender, recipientUsers, messageToSend, LocalDateTime.now(), false);
		messageRepository.save(message);
		return "actualUser";
	}

	@RequestMapping("/upload")
	public ResponseEntity<String> uploadTarantula(@RequestPart("images") List<MultipartFile> images,
			@ModelAttribute Tarantula tarantula) throws IOException {

		if (tarantulaRepository.findByGenusAndSpieces(tarantula.getGenus(), tarantula.getSpieces()).isPresent()) {
			return ResponseEntity.badRequest().body("This spider already exist!");
		}

		String tarantulaName = tarantula.getGenus() + "_" + tarantula.getSpieces();
		int index = tarantula.getImagesNumber();
		for (MultipartFile multipartFile : images) {

			File file = convertMultipartFileToFile(multipartFile, tarantulaName, index);
			FileEntity fileEntity = new FileEntity();

			fileEntity.setTarantulaID(tarantulaRepository.save(tarantula).getId());
			fileEntity.setPath(imagesPathToDB + file.getName());

			fileEntityRepository.save(fileEntity);
			index++;

		}
		tarantula.setImagesNumber(index);
		tarantulaRepository.save(tarantula);

		return ResponseEntity.ok().body("Saved");
	}

	@PostMapping("/uploadPictures")
	public ResponseEntity<String> uploadPicture(@RequestPart("images") List<MultipartFile> images,
			@RequestParam("tarantulaId") Long id) {

		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();

		if (authentication == null) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
		}

		if (images == null || images.size() == 0) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No images selected");
		}

		Tarantula tarantula = tarantulaRepository.findById(id).get();

		String tarantulaName = tarantula.getGenus() + "_" + tarantula.getSpieces();
		int index = tarantula.getImagesNumber();
		for (MultipartFile multipartFile : images) {

			File file = convertMultipartFileToFile(multipartFile, tarantulaName, index);
			FileEntity fileEntity = new FileEntity();

			fileEntity.setTarantulaID(tarantula.getId());
			fileEntity.setPath(imagesPathToDB + file.getName());

			fileEntityRepository.save(fileEntity);
			index++;

		}
		tarantula.setImagesNumber(index);

		tarantulaRepository.save(tarantula);

		return ResponseEntity.ok().body("Images are saved");
	}

	private File convertMultipartFileToFile(MultipartFile multipartFile, String tarantulaName, int number) {
		File file = null;
		try {
			String userHome = System.getProperty("user.home");
			String imageLocation = userHome + projectLocation + imagesPath + tarantulaName + number + JPGExtension;

			file = new File(imageLocation);

			multipartFile.transferTo(file);
		} catch (IOException e) {
			// itt majd egy kivétel dobásnak kell történnie
			System.err.println("Hiba történt a fájl létrehozása során: " + e.getMessage());
		}
		return file;
	}

	private Set<User> createUseresFromUsernames(String recipients) {
		Set<User> result = new HashSet<>();
		for (String username : recipients.split(",")) {
			User user = userRepository.findByUsername(username.trim());
			if (user == null) {
				throw new UsernameNotFoundException();
			}
			result.add(user);
		}

		return result;
	}

	private Set<User> createUseresFromEmails(String recipients) {
		Set<User> result = new HashSet<>();
		for (String email : recipients.split(",")) {
			User user = userRepository.findByEmail(email.trim());
			if (user == null) {
				throw new EmailNotFoundException();
			}
			result.add(user);
		}

		return result;
	}
}