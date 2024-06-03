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
	private static final String imagesPath="\\src\\main\\resources\\static\\img\\uploadedImages\\";
	private static final String projectLocation = "\\eclipse-workspace\\tarantulaForum";
	private static final String JPGExtension = ".jpg";
	private User userForRegistration;

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private TarantulaRepository tarantulaRepository;
	@Autowired
	private FileEntityRepository fileEntityRepository;

	
	@RequestMapping("/login")
	public String login() {
		
		return "actualUser";
	}
	
	@GetMapping("/actualUser")
	public String renderActualUser() {
	    // Itt visszaadhatod a megfelelő nézetet vagy végrehajthatod a szükséges logikát
	    return "actualUser";
	}
	@GetMapping("/tarantula")
	public String renderTarantula(@RequestParam("id") Long id, Model model) {
		Tarantula tarantula = tarantulaRepository.findById(id).get();
		String path = fileEntityRepository.findFirstByTarantulaIDOrderByIdAsc(id).get().getPath();
		path = path.replace("\\", "/");
	
		System.out.println(path);
		
		model.addAttribute("tarantula", tarantula);
		model.addAttribute("imagePath", path.substring(19));
		return "tarantula";
	}
	
	@RequestMapping("/index")
	public String renderIndex() {
		
		return "index";
	}
	
	@RequestMapping("/register")
	public String renderRegister() {
		System.out.println("r");
		return "register";
	}
	
	@RequestMapping("/registerUser")
	public String registerUser(@ModelAttribute User user) {
		userForRegistration = user;
		System.out.println("ru");
		return "registerAddress";
	}
	@RequestMapping("/registerAddress")
	public String registerAdress(@ModelAttribute Address address) {
		System.out.println("ADD");
		if(userForRegistration != null) {
			address.setUser(userForRegistration);
			userForRegistration.setAddresses(List.of(address));
			userServiceImpl.saveUser(userForRegistration);
			userForRegistration = null;
			System.out.println("thx");
			return "thanks";	
		}
		System.out.println("err");
		return "registrationError";
	} 
	
	@RequestMapping("/tarantulas")
	public String listTarantulas(Model model) {
		List<Tarantula> tarantulas = tarantulaRepository.findAll();
		model.addAttribute("tarantulas", tarantulas);
		return "tarantulas";
	}
	
	@RequestMapping("/uploadTarantula")
	public String renderTarantulaUlpoad(Model model) {
		
		return "tarantulaUpload";
	}

	@RequestMapping("/upload")
	public ResponseEntity<String> uploadTarantula(@RequestPart("images") List<MultipartFile> images,
									@ModelAttribute Tarantula tarantula) throws IOException {
		
		if(tarantulaRepository.findByGenusAndSpieces(tarantula.getGenus(), tarantula.getSpieces()).isPresent()) {
			 return ResponseEntity.badRequest().body("This spider already exist!");
		}
		// itt egy ciklus kell, hogy feldolgozzuk a bejövő képeket !!!!
		String tarantulaName = tarantula.getGenus()+"_"+tarantula.getSpieces();
	    File file = convertMultipartFileToFile(images.get(0),tarantulaName);
	    FileEntity fileEntity = new FileEntity();
	    
	    fileEntity.setTarantulaID(tarantulaRepository.save(tarantula).getId());
	    fileEntity.setPath(imagesPath+file.getName());
	    
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
	
	private File convertMultipartFileToFile(MultipartFile multipartFile, String tarantulaName) {
	    File file = null;
	    try {
	    	String userHome = System.getProperty("user.home");
	        // ITT KELL MAJD EGY LOGIKA AMI ELNEVEZI A KÉPEKET ILLETVE ELŐÁLLTJA A MEGFELELŐ ELÉRÉSI UTAT
	    	//plusz külön mappákba kell tenni a különböző fajokat azon belül külön mappa a nemeknek :  static/img/uploadedImages/GENICULATA/MALE/spider.jpg
	    	String imageLocation = userHome+projectLocation+imagesPath+tarantulaName+"0"+JPGExtension;
	    	System.out.println(imageLocation);
	        file = new File(imageLocation);
	       
	        multipartFile.transferTo(file);
	    } catch (IOException e) {
	        System.err.println("Hiba történt a fájl létrehozása során: " + e.getMessage());
	    }
	    return file;
	}
}