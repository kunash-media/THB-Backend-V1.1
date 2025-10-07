package com.thb.bakery.service;

import com.thb.bakery.dto.request.ContactRequestDTO;
import com.thb.bakery.dto.response.ContactResponseDTO;

import java.util.List;

public interface ContactService {
    ContactResponseDTO saveContact(ContactRequestDTO contactRequestDTO);
    List<ContactResponseDTO> getAllContacts();

    ContactResponseDTO getContactById(Long formId);
    void deleteContact(Long formId);
}