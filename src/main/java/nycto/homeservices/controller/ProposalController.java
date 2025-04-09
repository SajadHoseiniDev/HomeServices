package nycto.homeservices.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nycto.homeservices.dto.proposalDto.ProposalCreateDto;
import nycto.homeservices.dto.proposalDto.ProposalResponseDto;
import nycto.homeservices.service.serviceInterface.ProposalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proposal")
@RequiredArgsConstructor
public class ProposalController {
    private final ProposalService proposalService;

    private static final Logger logger = LoggerFactory.getLogger(ProposalController.class);


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProposalResponseDto> createProposal(
            @Valid @RequestBody ProposalCreateDto createDto,
            @RequestParam Long specialistId) {
        logger.info("Received POST request to create proposal for specialistId: {}", specialistId);
        ProposalResponseDto responseDto = proposalService.createProposal(createDto, specialistId);
        logger.info("Proposal created successfully: {}", responseDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProposalResponseDto>> getProposalsByOrderId(@RequestParam Long orderId) {
        logger.info("Received GET request to fetch proposals for orderId: {}", orderId);
        List<ProposalResponseDto> proposals = proposalService.getProposalsByOrderId(orderId);
        logger.info("Proposals fetched successfully: {}", proposals);
        return ResponseEntity.ok(proposals);
    }

}
