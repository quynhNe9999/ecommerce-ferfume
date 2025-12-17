package com.quynhtd.ecommerce.mapper;

import com.quynhtd.ecommerce.domain.Perfume;
import com.quynhtd.ecommerce.dto.HeaderResponse;
import com.quynhtd.ecommerce.dto.perfume.FullPerfumeResponse;
import com.quynhtd.ecommerce.dto.perfume.PerfumeRequest;
import com.quynhtd.ecommerce.dto.perfume.PerfumeResponse;
import com.quynhtd.ecommerce.dto.perfume.PerfumeSearchRequest;
import com.quynhtd.ecommerce.enums.SearchPerfume;
import com.quynhtd.ecommerce.exception.InputFieldException;
import com.quynhtd.ecommerce.repository.projection.PerfumeProjection;
import com.quynhtd.ecommerce.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PerfumeMapper {

    private final CommonMapper commonMapper;
    private final PerfumeService perfumeService;

    public FullPerfumeResponse getPerfumeById(Long perfumeId) {
        return commonMapper.convertToResponse(perfumeService.getPerfumeById(perfumeId), FullPerfumeResponse.class);
    }

    public List<PerfumeResponse> getPerfumesByIds(List<Long> perfumesId) {
        return commonMapper.convertToResponseList(perfumeService.getPerfumesByIds(perfumesId), PerfumeResponse.class);
    }

    public HeaderResponse<PerfumeResponse> getAllPerfumes(Pageable pageable) {
        Page<PerfumeProjection> perfumes = perfumeService.getAllPerfumes(pageable);
        return commonMapper.getHeaderResponse(perfumes.getContent(), perfumes.getTotalPages(), perfumes.getTotalElements(), PerfumeResponse.class);
    }

    public HeaderResponse<PerfumeResponse> findPerfumesByFilterParams(PerfumeSearchRequest filter, Pageable pageable) {
        Page<PerfumeProjection> perfumes = perfumeService.findPerfumesByFilterParams(filter, pageable);
        return commonMapper.getHeaderResponse(perfumes.getContent(), perfumes.getTotalPages(), perfumes.getTotalElements(), PerfumeResponse.class);
    }

    public List<PerfumeResponse> findByPerfumer(String perfumer) {
        return commonMapper.convertToResponseList(perfumeService.findByPerfumer(perfumer), PerfumeResponse.class);
    }

    public List<PerfumeResponse> findByPerfumeGender(String perfumeGender) {
        return commonMapper.convertToResponseList(perfumeService.findByPerfumeGender(perfumeGender), PerfumeResponse.class);
    }
    
    public HeaderResponse<PerfumeResponse> findByInputText(SearchPerfume searchType, String text, Pageable pageable) {
        Page<PerfumeProjection> perfumes = perfumeService.findByInputText(searchType, text, pageable);
        return commonMapper.getHeaderResponse(perfumes.getContent(), perfumes.getTotalPages(), perfumes.getTotalElements(), PerfumeResponse.class);
    }

    public FullPerfumeResponse savePerfume(PerfumeRequest perfumeRequest, MultipartFile file, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InputFieldException(bindingResult);
        }
        Perfume perfume = commonMapper.convertToEntity(perfumeRequest, Perfume.class);
        return commonMapper.convertToResponse(perfumeService.savePerfume(perfume, file), FullPerfumeResponse.class);
    }

    public String deletePerfume(Long perfumeId) {
        return perfumeService.deletePerfume(perfumeId);
    }
}
