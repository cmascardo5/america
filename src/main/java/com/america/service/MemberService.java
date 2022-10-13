package com.america.service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.america.domain.Member;
import com.america.repository.MemberRepository;
import com.america.repository.search.MemberSearchRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Member}.
 */
@Service
@Transactional
public class MemberService {

    private final Logger log = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;

    private final MemberSearchRepository memberSearchRepository;

    public MemberService(MemberRepository memberRepository, MemberSearchRepository memberSearchRepository) {
        this.memberRepository = memberRepository;
        this.memberSearchRepository = memberSearchRepository;
    }

    /**
     * Save a member.
     *
     * @param member the entity to save.
     * @return the persisted entity.
     */
    public Member save(Member member) {
        log.debug("Request to save Member : {}", member);
        Member result = memberRepository.save(member);
        memberSearchRepository.save(result);
        return result;
    }

    /**
     * Update a member.
     *
     * @param member the entity to save.
     * @return the persisted entity.
     */
    public Member update(Member member) {
        log.debug("Request to save Member : {}", member);
        Member result = memberRepository.save(member);
        memberSearchRepository.save(result);
        return result;
    }

    /**
     * Partially update a member.
     *
     * @param member the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Member> partialUpdate(Member member) {
        log.debug("Request to partially update Member : {}", member);

        return memberRepository
            .findById(member.getId())
            .map(existingMember -> {
                if (member.getFirstName() != null) {
                    existingMember.setFirstName(member.getFirstName());
                }
                if (member.getLastName() != null) {
                    existingMember.setLastName(member.getLastName());
                }
                if (member.getBirthDate() != null) {
                    existingMember.setBirthDate(member.getBirthDate());
                }
                if (member.getGender() != null) {
                    existingMember.setGender(member.getGender());
                }
                if (member.getAddress() != null) {
                    existingMember.setAddress(member.getAddress());
                }
                if (member.getCity() != null) {
                    existingMember.setCity(member.getCity());
                }
                if (member.getState() != null) {
                    existingMember.setState(member.getState());
                }
                if (member.getZip() != null) {
                    existingMember.setZip(member.getZip());
                }
                if (member.getCounty() != null) {
                    existingMember.setCounty(member.getCounty());
                }
                if (member.getCountry() != null) {
                    existingMember.setCountry(member.getCountry());
                }
                if (member.getTobaccoUseIndicator() != null) {
                    existingMember.setTobaccoUseIndicator(member.getTobaccoUseIndicator());
                }
                if (member.getSubstanceAbuseIndicator() != null) {
                    existingMember.setSubstanceAbuseIndicator(member.getSubstanceAbuseIndicator());
                }
                if (member.getLastUpdateDatetime() != null) {
                    existingMember.setLastUpdateDatetime(member.getLastUpdateDatetime());
                }

                return existingMember;
            })
            .map(memberRepository::save)
            .map(savedMember -> {
                memberSearchRepository.save(savedMember);

                return savedMember;
            });
    }

    /**
     * Get all the members.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Member> findAll(Pageable pageable) {
        log.debug("Request to get all Members");
        return memberRepository.findAll(pageable);
    }

    /**
     * Get one member by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Member> findOne(Long id) {
        log.debug("Request to get Member : {}", id);
        return memberRepository.findById(id);
    }

    /**
     * Delete the member by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Member : {}", id);
        memberRepository.deleteById(id);
        memberSearchRepository.deleteById(id);
    }

    /**
     * Search for the member corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Member> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Members for query {}", query);
        return memberSearchRepository.search(query, pageable);
    }
}
