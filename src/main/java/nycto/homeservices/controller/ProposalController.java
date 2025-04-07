package nycto.homeservices.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.proposalDto.ProposalCreateDto;
import nycto.homeservices.dto.proposalDto.ProposalResponseDto;
import nycto.homeservices.service.serviceInterface.ProposalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proposal")
@RequiredArgsConstructor
public class ProposalController {
    private final ProposalService proposalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProposalResponseDto> createProposal(
            @Valid @RequestBody ProposalCreateDto createDto,
            @RequestParam Long specialistId) {
        ProposalResponseDto responseDto = proposalService.createProposal(createDto, specialistId);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProposalResponseDto>> getProposalsByOrderId(@RequestParam Long orderId) {
        List<ProposalResponseDto> proposals = proposalService.getProposalsByOrderId(orderId);
        return ResponseEntity.ok(proposals);
    }

}
