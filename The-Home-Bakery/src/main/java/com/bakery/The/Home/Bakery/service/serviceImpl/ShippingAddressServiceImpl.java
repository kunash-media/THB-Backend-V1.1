package com.bakery.The.Home.Bakery.service.serviceImpl;

import com.bakery.The.Home.Bakery.dto.request.ShippingAddressDTO;
import com.bakery.The.Home.Bakery.entity.ShippingAddressEntity;
import com.bakery.The.Home.Bakery.entity.UserEntity;
import com.bakery.The.Home.Bakery.repository.ShippingAddressRepository;
import com.bakery.The.Home.Bakery.repository.UserRepository;
import com.bakery.The.Home.Bakery.service.ShippingAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShippingAddressServiceImpl implements ShippingAddressService {

    private static final Logger logger = LoggerFactory.getLogger(ShippingAddressServiceImpl.class);

    private final ShippingAddressRepository addressRepository;
    private final UserRepository userRepository;

    public ShippingAddressServiceImpl(ShippingAddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ShippingAddressDTO createAddress(Long userId, ShippingAddressDTO addressDTO) {
        logger.info("Creating shipping address for user ID: {}", userId);
        logger.debug("Address details - City: {}, State: {}, Pincode: {}",
                addressDTO.getShippingCity(), addressDTO.getShippingState(), addressDTO.getShippingPincode());

        try {
            // Validate user exists
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> {
                        logger.error("User not found with ID: {}", userId);
                        return new RuntimeException("User not found");
                    });

            logger.debug("Found user: {} for address creation", user.getEmail());

            // Validate address data
            validateAddressDTO(addressDTO);

            ShippingAddressEntity addressEntity = new ShippingAddressEntity();
            BeanUtils.copyProperties(addressDTO, addressEntity);
            addressEntity.setUser(user);

            addressEntity = addressRepository.save(addressEntity);
            logger.debug("Shipping address saved with ID: {}", addressEntity.getShippingId());

            addressDTO.setShippingId(addressEntity.getShippingId());
            logger.info("Shipping address created successfully with ID: {} for user ID: {}",
                    addressEntity.getShippingId(), userId);

            return addressDTO;

        } catch (RuntimeException e) {
            logger.error("Error creating shipping address for user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error creating shipping address for user ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to create shipping address: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ShippingAddressDTO> getAddressesByUserId(Long userId) {
        logger.info("Fetching shipping addresses for user ID: {}", userId);

        try {
            // Validate user exists
            if (!userRepository.existsById(userId)) {
                logger.warn("User not found with ID: {}", userId);
                throw new RuntimeException("User not found with ID: " + userId);
            }

            List<ShippingAddressEntity> addressEntities = addressRepository.findByUserUserId(userId);
            logger.debug("Found {} shipping addresses for user ID: {}", addressEntities.size(), userId);

            List<ShippingAddressDTO> addresses = addressEntities.stream()
                    .map(addressEntity -> {
                        ShippingAddressDTO dto = new ShippingAddressDTO();
                        BeanUtils.copyProperties(addressEntity, dto);
                        return dto;
                    })
                    .collect(Collectors.toList());

            logger.info("Successfully retrieved {} addresses for user ID: {}", addresses.size(), userId);
            return addresses;

        } catch (RuntimeException e) {
            logger.error("Error fetching shipping addresses for user ID {}: {}", userId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fetching shipping addresses for user ID {}: {}", userId, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch shipping addresses: " + e.getMessage(), e);
        }
    }

    @Override
    public List<ShippingAddressDTO> getAllAddresses() {
        logger.info("Fetching all shipping addresses");

        try {
            List<ShippingAddressEntity> addressEntities = addressRepository.findAll();
            logger.debug("Found {} shipping addresses in total", addressEntities.size());

            List<ShippingAddressDTO> addresses = addressEntities.stream()
                    .map(addressEntity -> {
                        ShippingAddressDTO dto = new ShippingAddressDTO();
                        BeanUtils.copyProperties(addressEntity, dto);
                        logger.trace("Converted address ID: {} to DTO", addressEntity.getShippingId());
                        return dto;
                    })
                    .collect(Collectors.toList());

            logger.info("Successfully retrieved all {} shipping addresses", addresses.size());
            return addresses;

        } catch (Exception e) {
            logger.error("Error fetching all shipping addresses: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch all shipping addresses: " + e.getMessage(), e);
        }
    }

    @Override
    public ShippingAddressDTO updateAddress(Long userId, Long shippingId, ShippingAddressDTO addressDTO) {
        logger.info("Updating shipping address ID: {} for user ID: {}", shippingId, userId);
        logger.debug("Update details - City: {}, State: {}, Pincode: {}",
                addressDTO.getShippingCity(), addressDTO.getShippingState(), addressDTO.getShippingPincode());

        try {
            // Validate address exists
            ShippingAddressEntity addressEntity = addressRepository.findById(shippingId)
                    .orElseThrow(() -> {
                        logger.error("Shipping address not found with ID: {}", shippingId);
                        return new RuntimeException("Address not found");
                    });

            logger.debug("Found existing address for user ID: {}", addressEntity.getUser().getUserId());

            // Validate address belongs to user
            if (!addressEntity.getUser().getUserId().equals(userId)) {
                logger.error("Address ID: {} does not belong to user ID: {}", shippingId, userId);
                throw new RuntimeException("Address does not belong to user");
            }

            // Validate update data
            validateAddressUpdateDTO(addressDTO);

            int updateCount = 0;

            // Update only non-null fields from the DTO
            if (StringUtils.hasText(addressDTO.getCustomerPhone())) {
                addressEntity.setCustomerPhone(addressDTO.getCustomerPhone());
                updateCount++;
                logger.debug("Updated customer phone");
            }
            if (StringUtils.hasText(addressDTO.getCustomerEmail())) {
                addressEntity.setCustomerEmail(addressDTO.getCustomerEmail());
                updateCount++;
                logger.debug("Updated customer email");
            }
            if (StringUtils.hasText(addressDTO.getShippingAddress())) {
                addressEntity.setShippingAddress(addressDTO.getShippingAddress());
                updateCount++;
                logger.debug("Updated shipping address");
            }
            if (StringUtils.hasText(addressDTO.getShippingCity())) {
                addressEntity.setShippingCity(addressDTO.getShippingCity());
                updateCount++;
                logger.debug("Updated shipping city");
            }
            if (StringUtils.hasText(addressDTO.getShippingState())) {
                addressEntity.setShippingState(addressDTO.getShippingState());
                updateCount++;
                logger.debug("Updated shipping state");
            }
            if (StringUtils.hasText(addressDTO.getShippingPincode())) {
                addressEntity.setShippingPincode(addressDTO.getShippingPincode());
                updateCount++;
                logger.debug("Updated shipping pincode");
            }
            if (StringUtils.hasText(addressDTO.getShippingCountry())) {
                addressEntity.setShippingCountry(addressDTO.getShippingCountry());
                updateCount++;
                logger.debug("Updated shipping country");
            }

            if (updateCount == 0) {
                logger.warn("No fields to update for address ID: {}", shippingId);
                throw new RuntimeException("No valid fields provided for update");
            }

            addressEntity = addressRepository.save(addressEntity);
            logger.debug("Shipping address updated with {} field changes", updateCount);

            ShippingAddressDTO updatedDTO = new ShippingAddressDTO();
            BeanUtils.copyProperties(addressEntity, updatedDTO);

            logger.info("Shipping address ID: {} updated successfully for user ID: {}", shippingId, userId);
            return updatedDTO;

        } catch (RuntimeException e) {
            logger.error("Error updating shipping address ID {} for user ID {}: {}",
                    shippingId, userId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating shipping address ID {} for user ID {}: {}",
                    shippingId, userId, e.getMessage(), e);
            throw new RuntimeException("Failed to update shipping address: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteAddress(Long userId, Long shippingId) {
        logger.info("Deleting shipping address ID: {} for user ID: {}", shippingId, userId);

        try {
            // Validate address exists
            ShippingAddressEntity addressEntity = addressRepository.findById(shippingId)
                    .orElseThrow(() -> {
                        logger.error("Shipping address not found with ID: {}", shippingId);
                        return new RuntimeException("Address not found");
                    });

            logger.debug("Found address to delete for user ID: {}", addressEntity.getUser().getUserId());

            // Validate address belongs to user
            if (!addressEntity.getUser().getUserId().equals(userId)) {
                logger.error("Address ID: {} does not belong to user ID: {}", shippingId, userId);
                throw new RuntimeException("Address does not belong to user");
            }

            addressRepository.delete(addressEntity);
            logger.info("Shipping address ID: {} deleted successfully for user ID: {}", shippingId, userId);

        } catch (RuntimeException e) {
            logger.error("Error deleting shipping address ID {} for user ID {}: {}",
                    shippingId, userId, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error deleting shipping address ID {} for user ID {}: {}",
                    shippingId, userId, e.getMessage(), e);
            throw new RuntimeException("Failed to delete shipping address: " + e.getMessage(), e);
        }
    }

    // Private validation methods
    private void validateAddressDTO(ShippingAddressDTO addressDTO) {
        logger.debug("Validating shipping address DTO");

        if (addressDTO == null) {
            throw new RuntimeException("Address data cannot be null");
        }

        List<String> validationErrors = new java.util.ArrayList<>();

        if (!StringUtils.hasText(addressDTO.getCustomerPhone())) {
            validationErrors.add("Customer phone is required");
        }
        if (!StringUtils.hasText(addressDTO.getShippingAddress())) {
            validationErrors.add("Shipping address is required");
        }
        if (!StringUtils.hasText(addressDTO.getShippingCity())) {
            validationErrors.add("Shipping city is required");
        }
        if (!StringUtils.hasText(addressDTO.getShippingState())) {
            validationErrors.add("Shipping state is required");
        }
        if (!StringUtils.hasText(addressDTO.getShippingPincode())) {
            validationErrors.add("Shipping pincode is required");
        }
        if (!StringUtils.hasText(addressDTO.getShippingCountry())) {
            validationErrors.add("Shipping country is required");
        }

        if (!validationErrors.isEmpty()) {
            String errorMessage = String.join(", ", validationErrors);
            logger.warn("Address validation failed: {}", errorMessage);
            throw new RuntimeException(errorMessage);
        }

        logger.debug("Address validation passed");
    }

    private void validateAddressUpdateDTO(ShippingAddressDTO addressDTO) {
        logger.debug("Validating shipping address update DTO");

        if (addressDTO == null) {
            throw new RuntimeException("Address update data cannot be null");
        }

        // For updates, at least one field should be provided
        boolean hasValidField = StringUtils.hasText(addressDTO.getCustomerPhone()) ||
                StringUtils.hasText(addressDTO.getCustomerEmail()) ||
                StringUtils.hasText(addressDTO.getShippingAddress()) ||
                StringUtils.hasText(addressDTO.getShippingCity()) ||
                StringUtils.hasText(addressDTO.getShippingState()) ||
                StringUtils.hasText(addressDTO.getShippingPincode()) ||
                StringUtils.hasText(addressDTO.getShippingCountry());

        if (!hasValidField) {
            logger.warn("No valid fields provided for address update");
            throw new RuntimeException("At least one field must be provided for update");
        }

        logger.debug("Address update validation passed");
    }
}
