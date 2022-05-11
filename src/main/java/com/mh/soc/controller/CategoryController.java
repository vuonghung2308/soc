package com.mh.soc.controller;

import com.mh.soc.interceptor.CustomException;
import com.mh.soc.model.Category;
import com.mh.soc.model.File;
import com.mh.soc.model.User;
import com.mh.soc.repository.CategoryRepository;
import com.mh.soc.repository.FileRepository;
import com.mh.soc.vo.response.CategoryResponse;
import com.mh.soc.vo.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private FileRepository fileRepo;

    @GetMapping("")
    public ResponseEntity<?> get() {
        List<Category> list = categoryRepo.findAll();
        return ResponseEntity.ok(CategoryResponse.get(list));
    }

    @PostMapping("create")
    public ResponseEntity<?> create(
            @RequestParam MultipartFile file,
            @RequestParam String name,
            HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("user");
        if (user.getRole() == User.Role.CUSTOMER) {
            throw new CustomException(
                    "ACTION_NOT_ALLOWED",
                    "Your action is not allowed"
            );
        }
        try {
            String originName = StringUtils.cleanPath(
                    Objects.requireNonNull(file.getOriginalFilename()));
            SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSSSSS");
            String ext = originName.substring(originName.lastIndexOf("."));
            String newName = "file_" + f.format(new Date()) + ext;
            File image = new File(newName, file.getContentType(), file.getBytes());
            Category category = new Category(name, newName);
            categoryRepo.save(category);
            fileRepo.save(image);

            ResponseMessage message = new ResponseMessage(
                    "CREATE_SUCCESSFULLY",
                    "Create category: " + name + " successfully"
            );
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(
                    "CANT_NOT_CREATE",
                    "Can not create category name: " + name
            );
        }
    }

    @PostMapping("update")
    public ResponseEntity<?> update(
            @RequestParam Long id,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String name,
            HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("user");
        if (user.getRole() == User.Role.CUSTOMER) {
            throw new CustomException(
                    "ACTION_NOT_ALLOWED",
                    "Your action is not allowed"
            );
        }
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new CustomException(
                        "CATEGORY_NOT_FOUND",
                        "Can not find category has id: " + id)
                );

        try {
            if (file != null) {
                String originName = StringUtils.cleanPath(
                        Objects.requireNonNull(file.getOriginalFilename()));
                SimpleDateFormat f = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSSSSS");
                String ext = originName.substring(originName.lastIndexOf("."));
                String newName = "file_" + f.format(new Date()) + ext;
                File image = new File(newName, file.getContentType(), file.getBytes());
                File oldFile = fileRepo.getByName(category.getIcon());
                category.setIcon(newName);
                fileRepo.delete(oldFile);
                fileRepo.save(image);
            }
            if (name != null) {
                category.setName(name);
            }
            categoryRepo.save(category);

            ResponseMessage message = new ResponseMessage(
                    "UPDATE_SUCCESSFULLY",
                    "Update category has id: " + id + " successfully"
            );
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(
                    "CANT_NOT_UPDATE",
                    "Can not update category has id: " + id
            );
        }
    }

    @PostMapping("delete")
    public ResponseEntity<?> delete(
            @RequestParam Long id,
            HttpServletRequest request
    ) {
        User user = (User) request.getAttribute("user");
        if (user.getRole() == User.Role.CUSTOMER) {
            throw new CustomException(
                    "ACTION_NOT_ALLOWED",
                    "Your action is not allowed"
            );
        }
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new CustomException(
                        "CATEGORY_NOT_FOUND",
                        "Can not find category has id: " + id)
                );
        File file = fileRepo.getByName(category.getIcon());
        categoryRepo.delete(category);
        fileRepo.delete(file);
        ResponseMessage message = new ResponseMessage(
                "DELETE_SUCCESSFULLY",
                "Delete category has id: " + id + " successfully"
        );
        return ResponseEntity.ok(message);
    }
}
