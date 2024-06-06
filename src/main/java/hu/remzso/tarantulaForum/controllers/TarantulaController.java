package hu.remzso.tarantulaForum.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import hu.remzso.tarantulaForum.entities.Tarantula;
import hu.remzso.tarantulaForum.entities.User;
import hu.remzso.tarantulaForum.repositories.FileEntityRepository;
import hu.remzso.tarantulaForum.repositories.TarantulaRepository;
import hu.remzso.tarantulaForum.services.UserServiceImpl;

@Controller
public class TarantulaController {
	private static final String imagesPath="\\src\\main\\resources\\static\\img\\uploadedImages\\";
	private static final String imagesPathToDB ="\\static\\img\\uploadedImages\\";
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
		for(FileEntity fileEntity : fileEntityRepository.findAllByTarantulaID(id).get()) {
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
		
		if(userForRegistration != null) {
			address.setUser(userForRegistration);
			userForRegistration.setAddresses(List.of(address));
			userServiceImpl.saveUser(userForRegistration);
			userForRegistration = null;
			
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
		int index = 0;
		for(MultipartFile multipartFile : images) {
		
			
			 File file = convertMultipartFileToFile(multipartFile,tarantulaName,index);
			    FileEntity fileEntity = new FileEntity();
			    
			    fileEntity.setTarantulaID(tarantulaRepository.save(tarantula).getId());
			    fileEntity.setPath(imagesPathToDB+file.getName());
			    
			    fileEntityRepository.save(fileEntity);
			    index++;
			
			
		}
	   
	    

	    return ResponseEntity.ok().body("Saved");
	}
	
	@PostMapping("/uploadPicture")
	public ResponseEntity<String> uploadPicture(@RequestPart("images") List<MultipartFile> images){
		
		return null;
	} 
	
	private File convertMultipartFileToFile(MultipartFile multipartFile, String tarantulaName, int number) {
	    File file = null;
	    try {
	    	String userHome = System.getProperty("user.home");
	    	String imageLocation = userHome+projectLocation+imagesPath+tarantulaName+number+JPGExtension;
	    	System.out.println(imageLocation);
	        file = new File(imageLocation);
	       
	        multipartFile.transferTo(file);
	    } catch (IOException e) {
	        System.err.println("Hiba történt a fájl létrehozása során: " + e.getMessage());
	    }
	    return file;
	}
}