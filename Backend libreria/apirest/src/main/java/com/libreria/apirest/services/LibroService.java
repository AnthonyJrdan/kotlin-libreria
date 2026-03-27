package com.libreria.apirest.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.libreria.apirest.DTO.libro.CreateLibroRequest;
import com.libreria.apirest.DTO.libro.LibroResponse;
import com.libreria.apirest.DTO.libro.UpdateLibroRequest;
import com.libreria.apirest.DTO.libro.mapper.LibroMapper;
import com.libreria.apirest.models.Categoria;
import com.libreria.apirest.models.Libro;
import com.libreria.apirest.repositories.CategoriaRepository;
import com.libreria.apirest.repositories.LibroRepository;

import jakarta.transaction.Transactional;

@Service
public class LibroService {
    
    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private LibroMapper libroMapper;

    @Transactional
    public LibroResponse create(CreateLibroRequest request) throws IOException{
        Categoria categoria = categoriaRepository.findById(request.getId_categoria()).orElseThrow(
            () -> new RuntimeException("La categoria no existe")
        );

        Libro libro = new Libro();
        libro.setCategoria(categoria);
        libro.setNombre(request.getNombre());
        libro.setAutor(request.getAutor());
        libro.setDescipcion(request.getDescripcion());
        libro.setPrecio(request.getPrecio());

        Libro saveLibro = libroRepository.save(libro);
        MultipartFile[] archivos = request.getFiles();

        if(archivos != null && archivos.length > 0) {
            String uploadDir = "uploads/libros/" + saveLibro.getId();
            Files.createDirectories(Paths.get(uploadDir));

            if(archivos.length >= 1 && !archivos[0].isEmpty()){
                String fileName1 = archivos[0].getOriginalFilename();
                String filePath1 = Paths.get(uploadDir, fileName1).toString();
                Files.copy(archivos[0].getInputStream(), Paths.get(filePath1), StandardCopyOption.REPLACE_EXISTING);
                saveLibro.setImagen1("/" + filePath1.replace("\\", "/"));
            }
            if(archivos.length >= 2 && !archivos[1].isEmpty()){
                String fileName2 = archivos[1].getOriginalFilename();
                String filePath2 = Paths.get(uploadDir, fileName2).toString();
                Files.copy(archivos[1].getInputStream(), Paths.get(filePath2), StandardCopyOption.REPLACE_EXISTING);
                saveLibro.setImagen2("/" + filePath2.replace("\\", "/"));
            }

            saveLibro = libroRepository.save(saveLibro);
        }

        return libroMapper.tLibroResponse(saveLibro);
        
    }

    // !

    @Transactional
    public LibroResponse update(Long id, UpdateLibroRequest request) throws IOException{

        Libro libro= libroRepository.findById(id).orElseThrow(
            () -> new RuntimeException("El libro no existe")
        );


        libro.setNombre(request.getNombre());
        libro.setAutor(request.getAutor());
        libro.setDescipcion(request.getDescripcion());
        libro.setPrecio(request.getPrecio());

        MultipartFile[] archivos = request.getFiles();

        if(archivos != null && archivos.length > 0) {
            String uploadDir = "uploads/libros/" + libro.getId();
            Files.createDirectories(Paths.get(uploadDir));

            if(archivos.length >= 1 && !archivos[0].isEmpty()){
                String fileName1 = archivos[0].getOriginalFilename();
                String filePath1 = Paths.get(uploadDir, fileName1).toString();
                Files.copy(archivos[0].getInputStream(), Paths.get(filePath1), StandardCopyOption.REPLACE_EXISTING);
                libro.setImagen1("/" + filePath1.replace("\\", "/"));
            }
            if(archivos.length >= 2 && !archivos[1].isEmpty()){
                String fileName2 = archivos[1].getOriginalFilename();
                String filePath2 = Paths.get(uploadDir, fileName2).toString();
                Files.copy(archivos[1].getInputStream(), Paths.get(filePath2), StandardCopyOption.REPLACE_EXISTING);
                libro.setImagen2("/" + filePath2.replace("\\", "/"));
            }
        }

        Libro updateLibro = libroRepository.save(libro);

        return libroMapper.tLibroResponse(updateLibro);
        
    }

    // * obtener libro x categoria
    @Transactional
    public List<LibroResponse> findByCategoriaId(Long idCategoria) {
        List<Libro> libros = libroRepository.findByCategoriaId(idCategoria);
        return libros.stream().map(libroMapper::tLibroResponse).collect(Collectors.toList());
    }


    @Transactional
    public void delete(Long id){
        Libro libro= libroRepository.findById(id).orElseThrow(
            () -> new RuntimeException("El libro no existe")
        );

        libroRepository.delete(libro);
    }
}
