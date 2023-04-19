package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final UserService userService;
	private final RoleService roleService;

	public AdminController(UserService userService, RoleService roleService) {
	    this.userService = userService;
		this.roleService = roleService;
	}

    @GetMapping()
    public String show(Principal principal, Model model) {
        User admin = userService.findByUsername(principal.getName());
        model.addAttribute("admin", admin);
		return "admin";
    }

	@GetMapping("/users")
	public String allUsers(Model model){
		model.addAttribute("users", userService.allUsers());
		model.addAttribute("userRoles", roleService.getAllRoles());
		return "/users";
	}


	@GetMapping(value = "/user/{id}")
	public String showUser(@PathVariable("id") Long id, Model model){
		model.addAttribute("user", userService.findUserById(id));
		return "/user";
	}

	@GetMapping(value = "/add")
	public String newUser(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("roles", roleService.getAllRoles());
		return "add";
	}

	@PostMapping()
	public String addUser(@ModelAttribute("user") User user) {
		userService.saveUser(user);
		return "redirect:/admin/users";
	}

	@GetMapping("/{id}/update")
	public String edit(Model model, @PathVariable("id") Long id) {
		model.addAttribute("user", userService.findUserById(id));
		model.addAttribute("roles", roleService.getAllRoles());
		return "update";
	}

	@PatchMapping("/{id}")
	public String update(@ModelAttribute("user") User user, @PathVariable("id") Long id) {
		userService. update(user, id);
		return "redirect:/admin/users";
	}


	@DeleteMapping( "admin/delete/{id}")
	public String deleteUser (@PathVariable("id") Long id) {
		userService.deleteUser(id);
		return "redirect:/admin/users";
	}
}