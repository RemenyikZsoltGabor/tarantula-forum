package hu.remzso.tarantulaForum.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import hu.remzso.tarantulaForum.entities.Address;
import hu.remzso.tarantulaForum.entities.FileEntity;
import hu.remzso.tarantulaForum.entities.Tarantula;
import hu.remzso.tarantulaForum.entities.User;
import hu.remzso.tarantulaForum.repositories.FileEntityRepository;
import hu.remzso.tarantulaForum.repositories.TarantulaRepository;
import hu.remzso.tarantulaForum.services.UserServiceImpl;

@Controller
public class TarantulaController {
	private User userForRegistration;

	private List<Tarantula> tarantulas;
	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private TarantulaRepository tarantulaRepository;
	@Autowired
	private FileEntityRepository fileEntityRepository;

	
	@PostMapping("/login")
	public String login() {
		
		return "actualUser";
	}
	@GetMapping("/tarantula")
	public String renderTarantula(@RequestParam("id") Long id, Model model) {
		Tarantula tarantula = tarantulaRepository.findById(id).get();
		String path = fileEntityRepository.findFirstByOrderByIdAsc().getPath();
		System.out.println(path);
		
		model.addAttribute("tarantula", tarantula);
		model.addAttribute("image", path);
		return "tarantula";
	}
	
	@RequestMapping("/index")
	public String renderIndex() {
		if(tarantulas == null) {
			tarantulas = tarantulaRepository.findAll();
		}
		System.out.println("INDEX");
		return "index";
	}
	
	@RequestMapping("/register")
	public String renderRegister() {
		System.out.println("REGISTER");
		return "register";
	}
	
	@RequestMapping("/registerUser")
	public String registerUser(@ModelAttribute User user) {
		System.out.println("REGISTERUSER");
		userForRegistration = user;
		return "registerAddress";
	}
	@RequestMapping("/registerAddress")
	public String registerAdress(@ModelAttribute Address address) {
		System.out.println("ADDRESSREGISTER");
		if(userForRegistration != null) {
			address.setUser(userForRegistration);
			userForRegistration.setAddresses(List.of(address));
			userServiceImpl.saveUser(userForRegistration);
			userForRegistration = null;
			return "thanks";	
		}
		return "registrationError";
	}
	@GetMapping("/tarantulas")
	public String renderTarantulas() {
		System.out.println("TARANTULAs");
		return "tarantulas";
	}
	@RequestMapping("/listTarantulas")
	public String listTarantulas(Model model) {
		List<Tarantula> tarantulas = tarantulaRepository.findAll();
		System.out.println(tarantulas);
		model.addAttribute("tarantulas", tarantulas);
		return "tarantulas";
	}
	@RequestMapping("/uploadTarantula")
	public String renderTarantulaUlpoad(Model model) {
		System.out.println("tarantulaUpload");
		return "tarantulaUpload";
	}

	@RequestMapping("/upload")
	public ResponseEntity<String> uploadTarantula(@RequestPart("images") List<MultipartFile> images,
									@ModelAttribute Tarantula tarantula) throws IOException {
		
		if(tarantulaRepository.findByGenusAndSpieces(tarantula.getGenus(), tarantula.getSpieces()).isPresent()) {
			 return ResponseEntity.badRequest().body("Ez a pók már létezik!");
		}
	    File file = convertMultipartFileToFile(images.get(0));
	    FileEntity fileEntity = new FileEntity();
	    
	    fileEntity.setTarantulaID(tarantulaRepository.save(tarantula).getId());
	    fileEntity.setPath(file.getAbsolutePath());
	    fileEntityRepository.save(fileEntity);
	    
	    String srcPath = "src";

	    
	    String filename = file.getName();

	   
	    byte[] content = Files.readAllBytes(file.toPath());

	    try {
	        
	        Path filePath = Paths.get(srcPath, filename);

	        
	        Files.write(filePath, content);

	        System.out.println("A fájl sikeresen elmentve az src mappába: " + filePath.toString());
	    } catch (IOException e) {
	        System.err.println("Hiba történt a fájl mentése közben: " + e.getMessage());
	    }

	    return ResponseEntity.ok().body("Mentve");
	}
	
	@PostMapping("/uploadPicture")
	public ResponseEntity<String> uploadPicture(@RequestPart("images") List<MultipartFile> images){
		
		return null;
	} 
	
	private File convertMultipartFileToFile(MultipartFile multipartFile) {
	    File file = null;
	    try {
	        // ITT KELL MAJD EGY LOGIKA AMI ELNEVEZI A KÉPEKET ILLETVE ELŐÁLLTJA A MEGFELELŐ ELÉRÉSI UTAT
	        file = new File("C:\\Users\\rzesm\\eclipse-workspace\\tarantulaForum\\src\\main\\resources\\static\\img\\uploadedImages\\image.jpg");
	       
	        multipartFile.transferTo(file);
	    } catch (IOException e) {
	        System.err.println("Hiba történt a fájl létrehozása során: " + e.getMessage());
	    }
	    return file;
	}
}