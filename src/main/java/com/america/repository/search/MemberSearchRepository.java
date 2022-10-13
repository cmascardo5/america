package com.america.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.america.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Member} entity.
 */
public interface MemberSearchRepository extends ElasticsearchRepository<Member, Long>, MemberSearchRepositoryInternal {}

interface MemberSearchRepositoryInternal {
    Page<Member> search(String query, Pageable pageable);
}

class MemberSearchRepositoryInternalImpl implements MemberSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    MemberSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Member> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Member> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Member.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
