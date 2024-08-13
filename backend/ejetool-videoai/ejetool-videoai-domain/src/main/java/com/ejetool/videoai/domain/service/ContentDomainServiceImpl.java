// package com.ejetool.videoai.domain.service;

// import org.springframework.stereotype.Service;

// import com.ejetool.videoai.domain.entity.Content;
// import com.ejetool.videoai.domain.repository.ContentRepository;

// import lombok.RequiredArgsConstructor;

// @Service
// @RequiredArgsConstructor
// public class ContentDomainServiceImpl implements ContentDomainService {
//     private final ContentRepository contentRepository;

//     /**
//      * 컨텐츠 생성
//      */
//     @Override
//     public Content create(Content content) {
//         return this.contentRepository.save(content);
//     }

//     /**
//      * 컨텐츠 삭제
//      */
//     @Override
//     public void delete(long id) {
//         this.contentRepository.deleteById(id);
//     }

//     /**
//      * 컨텐츠 삭제
//      */
//     @Override
//     public void delete(Content content) {
//         this.contentRepository.delete(content);
//     }
// }
