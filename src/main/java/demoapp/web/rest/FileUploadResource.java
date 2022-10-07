package demoapp.web.rest;

import demoapp.service.StorageService;
import demoapp.web.rest.errors.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class FileUploadResource {

    private final StorageService storageService;

    public FileUploadResource(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/files")
    public ResponseEntity<?> listUploadedFiles(Model model) {
        List<String> fileName = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder.fromMethodName(FileUploadResource.class,"getFile",
                path.getFileName().toString()).build().toString();
            return filename;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(fileName);
//        model.addAttribute("files", storageService.loadAll()
//            .map(path -> MvcUriComponentsBuilder.fromMethodName(FileUploadResource.class,
//                "serveFile", path.getFileName().toString()).build().toUri().toString())
//            .collect(Collectors.toList())
//        );
//        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String filename) throws IOException {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok(Base64.getEncoder().encodeToString(file.getInputStream().readAllBytes()));
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
//            + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files, RedirectAttributes redirectAttributes) {
        String message = "";
        try {
            List<String> fileName = new ArrayList<>();
            Arrays.stream(files).forEach(file -> {
                storageService.store(file);
                fileName.add(file.getOriginalFilename());
            });
            return ResponseEntity.ok().body("Uploaded the files successfully: " + fileName);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Fail to upload files!");
        }
//        storageService.store(file);
//        redirectAttributes.addFlashAttribute("message", "You successfully uploaded" +
//            file.getOriginalFilename() + "!");
//        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
