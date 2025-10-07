package com.thb.bakery.controller;

import com.thb.bakery.dto.request.ContactRequestDTO;
import com.thb.bakery.dto.response.ContactResponseDTO;
import com.thb.bakery.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/submit-form")
    public ResponseEntity<String> submitForm(@Valid @RequestBody ContactRequestDTO contactRequestDTO) {
        contactService.saveContact(contactRequestDTO);
        return ResponseEntity.ok("Thank you for your message!");
    }

    @GetMapping("/get-all-contact-us")
    public ResponseEntity<List<ContactResponseDTO>> getAllContacts() {
        List<ContactResponseDTO> contacts = contactService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("get-by-formId/{formId}")
    public ResponseEntity<ContactResponseDTO> getContactById(@PathVariable Long formId) {
        ContactResponseDTO contact = contactService.getContactById(formId);
        return ResponseEntity.ok(contact);
    }

    @DeleteMapping("delete-by-formId/{formId}")
    public ResponseEntity<String> deleteContact(@PathVariable Long formId) {
        contactService.deleteContact(formId);
        return ResponseEntity.ok("Contact deleted successfully!");
    }


}