package nycto.homeservices.service;

import lombok.RequiredArgsConstructor;
import nycto.homeservices.repository.CommentRepository;
import nycto.homeservices.service.serviceInterface.CommentService;
import nycto.homeservices.util.ValidationUtil;
import nycto.homeservices.util.dtoMapper.CommentMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ValidationUtil validationUtil;
    private final CommentMapper commentMapper;


}
