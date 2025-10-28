    package com.thb.bakery.service.serviceImpl;

    import com.thb.bakery.dto.request.AddonDto;
    import com.thb.bakery.entity.Addon;
    import com.thb.bakery.repository.AddonRepository;
    import com.thb.bakery.service.AddonService;
    import org.springframework.beans.BeanUtils;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class AddonServiceImpl implements AddonService {

        @Autowired
        private AddonRepository addonRepository;

        @Override
        public List<AddonDto> getAllAddons() {
            List<Addon> addons = addonRepository.findAll();
            return addons.stream().map(this::convertToDto).collect(Collectors.toList());
        }

        @Override
        public AddonDto createAddon(AddonDto addonDto) {
            Addon addon = new Addon();
            BeanUtils.copyProperties(addonDto, addon);
            Addon savedAddon = addonRepository.save(addon);
            return convertToDto(savedAddon);
        }

        private AddonDto convertToDto(Addon addon) {
            AddonDto dto = new AddonDto();
            BeanUtils.copyProperties(addon, dto);
            return dto;
        }
    }