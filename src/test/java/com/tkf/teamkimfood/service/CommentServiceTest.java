package com.tkf.teamkimfood.service;

class CommentServiceTest {
//
//    @Mock
//    private CommentRepository commentRepository;
//
//    @InjectMocks
//    private CommentService commentService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    void saveComment() {
//        CommentDto commentDto = new CommentDto();
//        commentDto.setContent("Test comment");
//        commentDto.setCommentDate(LocalDateTime.now());
//
//        Comment comment = new Comment();
//        comment.setId(1L);
//        comment.setContent(commentDto.getContent());
//        comment.setCommentDate(commentDto.getCommentDate());
//
//        when(commentRepository.save(any())).thenReturn(comment);
//
//        CommentDto savedCommentDto = commentService.saveComment(commentDto);
//
//        assertEquals(comment.getId(), savedCommentDto.getId());
//        assertEquals(comment.getContent(), savedCommentDto.getContent());
//        assertEquals(comment.getCommentDate(), savedCommentDto.getCommentDate());
//
//        verify(commentRepository, times(1)).save(any());
//    }
//
//    @Test
//    void getCommentById() {
//        Long commentId = 1L;
//
//        Comment comment = new Comment();
//        comment.setId(commentId);
//        comment.setContent("Test comment");
//        comment.setCommentDate(LocalDateTime.now());
//
//        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
//
//        CommentDto commentDto = commentService.getCommentById(commentId);
//
//        assertEquals(comment.getId(), commentDto.getId());
//        assertEquals(comment.getContent(), commentDto.getContent());
//        assertEquals(comment.getCommentDate(), commentDto.getCommentDate());
//
//        verify(commentRepository, times(1)).findById(commentId);
//    }
//
//    @Test
//    public void testDeleteComment() {
//        Long commentId = 1L;
//
//        commentService.deleteComment(commentId);
//
//        verify(commentRepository, times(1)).deleteById(commentId);
//    }
}