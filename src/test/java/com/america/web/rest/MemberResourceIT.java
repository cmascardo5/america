package com.america.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.america.IntegrationTest;
import com.america.domain.Member;
import com.america.repository.MemberRepository;
import com.america.repository.search.MemberSearchRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MemberResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MemberResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_ZIP = "AAAAAAAAAA";
    private static final String UPDATED_ZIP = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TOBACCO_USE_INDICATOR = false;
    private static final Boolean UPDATED_TOBACCO_USE_INDICATOR = true;

    private static final Boolean DEFAULT_SUBSTANCE_ABUSE_INDICATOR = false;
    private static final Boolean UPDATED_SUBSTANCE_ABUSE_INDICATOR = true;

    private static final LocalDate DEFAULT_LAST_UPDATE_DATETIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LAST_UPDATE_DATETIME = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/members";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/members";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MemberRepository memberRepository;

    /**
     * This repository is mocked in the com.america.repository.search test package.
     *
     * @see com.america.repository.search.MemberSearchRepositoryMockConfiguration
     */
    @Autowired
    private MemberSearchRepository mockMemberSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMemberMockMvc;

    private Member member;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createEntity(EntityManager em) {
        Member member = new Member()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthDate(DEFAULT_BIRTH_DATE)
            .gender(DEFAULT_GENDER)
            .address(DEFAULT_ADDRESS)
            .city(DEFAULT_CITY)
            .state(DEFAULT_STATE)
            .zip(DEFAULT_ZIP)
            .county(DEFAULT_COUNTY)
            .country(DEFAULT_COUNTRY)
            .tobaccoUseIndicator(DEFAULT_TOBACCO_USE_INDICATOR)
            .substanceAbuseIndicator(DEFAULT_SUBSTANCE_ABUSE_INDICATOR)
            .lastUpdateDatetime(DEFAULT_LAST_UPDATE_DATETIME);
        return member;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Member createUpdatedEntity(EntityManager em) {
        Member member = new Member()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zip(UPDATED_ZIP)
            .county(UPDATED_COUNTY)
            .country(UPDATED_COUNTRY)
            .tobaccoUseIndicator(UPDATED_TOBACCO_USE_INDICATOR)
            .substanceAbuseIndicator(UPDATED_SUBSTANCE_ABUSE_INDICATOR)
            .lastUpdateDatetime(UPDATED_LAST_UPDATE_DATETIME);
        return member;
    }

    @BeforeEach
    public void initTest() {
        member = createEntity(em);
    }

    @Test
    @Transactional
    void createMember() throws Exception {
        int databaseSizeBeforeCreate = memberRepository.findAll().size();
        // Create the Member
        restMemberMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(member))
            )
            .andExpect(status().isCreated());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate + 1);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testMember.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMember.getBirthDate()).isEqualTo(DEFAULT_BIRTH_DATE);
        assertThat(testMember.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testMember.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testMember.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testMember.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testMember.getZip()).isEqualTo(DEFAULT_ZIP);
        assertThat(testMember.getCounty()).isEqualTo(DEFAULT_COUNTY);
        assertThat(testMember.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testMember.getTobaccoUseIndicator()).isEqualTo(DEFAULT_TOBACCO_USE_INDICATOR);
        assertThat(testMember.getSubstanceAbuseIndicator()).isEqualTo(DEFAULT_SUBSTANCE_ABUSE_INDICATOR);
        assertThat(testMember.getLastUpdateDatetime()).isEqualTo(DEFAULT_LAST_UPDATE_DATETIME);

        // Validate the Member in Elasticsearch
        verify(mockMemberSearchRepository, times(1)).save(testMember);
    }

    @Test
    @Transactional
    void createMemberWithExistingId() throws Exception {
        // Create the Member with an existing ID
        member.setId(1L);

        int databaseSizeBeforeCreate = memberRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMemberMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(member))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeCreate);

        // Validate the Member in Elasticsearch
        verify(mockMemberSearchRepository, times(0)).save(member);
    }

    @Test
    @Transactional
    void getAllMembers() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get all the memberList
        restMemberMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP)))
            .andExpect(jsonPath("$.[*].county").value(hasItem(DEFAULT_COUNTY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].tobaccoUseIndicator").value(hasItem(DEFAULT_TOBACCO_USE_INDICATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].substanceAbuseIndicator").value(hasItem(DEFAULT_SUBSTANCE_ABUSE_INDICATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdateDatetime").value(hasItem(DEFAULT_LAST_UPDATE_DATETIME.toString())));
    }

    @Test
    @Transactional
    void getMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        // Get the member
        restMemberMockMvc
            .perform(get(ENTITY_API_URL_ID, member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(member.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthDate").value(DEFAULT_BIRTH_DATE.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.zip").value(DEFAULT_ZIP))
            .andExpect(jsonPath("$.county").value(DEFAULT_COUNTY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.tobaccoUseIndicator").value(DEFAULT_TOBACCO_USE_INDICATOR.booleanValue()))
            .andExpect(jsonPath("$.substanceAbuseIndicator").value(DEFAULT_SUBSTANCE_ABUSE_INDICATOR.booleanValue()))
            .andExpect(jsonPath("$.lastUpdateDatetime").value(DEFAULT_LAST_UPDATE_DATETIME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingMember() throws Exception {
        // Get the member
        restMemberMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member
        Member updatedMember = memberRepository.findById(member.getId()).get();
        // Disconnect from session so that the updates on updatedMember are not directly saved in db
        em.detach(updatedMember);
        updatedMember
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zip(UPDATED_ZIP)
            .county(UPDATED_COUNTY)
            .country(UPDATED_COUNTRY)
            .tobaccoUseIndicator(UPDATED_TOBACCO_USE_INDICATOR)
            .substanceAbuseIndicator(UPDATED_SUBSTANCE_ABUSE_INDICATOR)
            .lastUpdateDatetime(UPDATED_LAST_UPDATE_DATETIME);

        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedMember.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedMember))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMember.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMember.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testMember.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testMember.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testMember.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testMember.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testMember.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testMember.getCounty()).isEqualTo(UPDATED_COUNTY);
        assertThat(testMember.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testMember.getTobaccoUseIndicator()).isEqualTo(UPDATED_TOBACCO_USE_INDICATOR);
        assertThat(testMember.getSubstanceAbuseIndicator()).isEqualTo(UPDATED_SUBSTANCE_ABUSE_INDICATOR);
        assertThat(testMember.getLastUpdateDatetime()).isEqualTo(UPDATED_LAST_UPDATE_DATETIME);

        // Validate the Member in Elasticsearch
        verify(mockMemberSearchRepository).save(testMember);
    }

    @Test
    @Transactional
    void putNonExistingMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();
        member.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, member.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(member))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Member in Elasticsearch
        verify(mockMemberSearchRepository, times(0)).save(member);
    }

    @Test
    @Transactional
    void putWithIdMismatchMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();
        member.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(member))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Member in Elasticsearch
        verify(mockMemberSearchRepository, times(0)).save(member);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();
        member.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(member))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Member in Elasticsearch
        verify(mockMemberSearchRepository, times(0)).save(member);
    }

    @Test
    @Transactional
    void partialUpdateMemberWithPatch() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member using partial update
        Member partialUpdatedMember = new Member();
        partialUpdatedMember.setId(member.getId());

        partialUpdatedMember
            .firstName(UPDATED_FIRST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .zip(UPDATED_ZIP)
            .county(UPDATED_COUNTY)
            .tobaccoUseIndicator(UPDATED_TOBACCO_USE_INDICATOR);

        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMember.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMember))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMember.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testMember.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testMember.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testMember.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testMember.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testMember.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testMember.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testMember.getCounty()).isEqualTo(UPDATED_COUNTY);
        assertThat(testMember.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testMember.getTobaccoUseIndicator()).isEqualTo(UPDATED_TOBACCO_USE_INDICATOR);
        assertThat(testMember.getSubstanceAbuseIndicator()).isEqualTo(DEFAULT_SUBSTANCE_ABUSE_INDICATOR);
        assertThat(testMember.getLastUpdateDatetime()).isEqualTo(DEFAULT_LAST_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void fullUpdateMemberWithPatch() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeUpdate = memberRepository.findAll().size();

        // Update the member using partial update
        Member partialUpdatedMember = new Member();
        partialUpdatedMember.setId(member.getId());

        partialUpdatedMember
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthDate(UPDATED_BIRTH_DATE)
            .gender(UPDATED_GENDER)
            .address(UPDATED_ADDRESS)
            .city(UPDATED_CITY)
            .state(UPDATED_STATE)
            .zip(UPDATED_ZIP)
            .county(UPDATED_COUNTY)
            .country(UPDATED_COUNTRY)
            .tobaccoUseIndicator(UPDATED_TOBACCO_USE_INDICATOR)
            .substanceAbuseIndicator(UPDATED_SUBSTANCE_ABUSE_INDICATOR)
            .lastUpdateDatetime(UPDATED_LAST_UPDATE_DATETIME);

        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMember.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMember))
            )
            .andExpect(status().isOk());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);
        Member testMember = memberList.get(memberList.size() - 1);
        assertThat(testMember.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testMember.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testMember.getBirthDate()).isEqualTo(UPDATED_BIRTH_DATE);
        assertThat(testMember.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testMember.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testMember.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testMember.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testMember.getZip()).isEqualTo(UPDATED_ZIP);
        assertThat(testMember.getCounty()).isEqualTo(UPDATED_COUNTY);
        assertThat(testMember.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testMember.getTobaccoUseIndicator()).isEqualTo(UPDATED_TOBACCO_USE_INDICATOR);
        assertThat(testMember.getSubstanceAbuseIndicator()).isEqualTo(UPDATED_SUBSTANCE_ABUSE_INDICATOR);
        assertThat(testMember.getLastUpdateDatetime()).isEqualTo(UPDATED_LAST_UPDATE_DATETIME);
    }

    @Test
    @Transactional
    void patchNonExistingMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();
        member.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, member.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(member))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Member in Elasticsearch
        verify(mockMemberSearchRepository, times(0)).save(member);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();
        member.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(member))
            )
            .andExpect(status().isBadRequest());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Member in Elasticsearch
        verify(mockMemberSearchRepository, times(0)).save(member);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMember() throws Exception {
        int databaseSizeBeforeUpdate = memberRepository.findAll().size();
        member.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMemberMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(member))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Member in the database
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Member in Elasticsearch
        verify(mockMemberSearchRepository, times(0)).save(member);
    }

    @Test
    @Transactional
    void deleteMember() throws Exception {
        // Initialize the database
        memberRepository.saveAndFlush(member);

        int databaseSizeBeforeDelete = memberRepository.findAll().size();

        // Delete the member
        restMemberMockMvc
            .perform(delete(ENTITY_API_URL_ID, member.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Member> memberList = memberRepository.findAll();
        assertThat(memberList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Member in Elasticsearch
        verify(mockMemberSearchRepository, times(1)).deleteById(member.getId());
    }

    @Test
    @Transactional
    void searchMember() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        memberRepository.saveAndFlush(member);
        when(mockMemberSearchRepository.search("id:" + member.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(member), PageRequest.of(0, 1), 1));

        // Search the member
        restMemberMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + member.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(member.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthDate").value(hasItem(DEFAULT_BIRTH_DATE.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].zip").value(hasItem(DEFAULT_ZIP)))
            .andExpect(jsonPath("$.[*].county").value(hasItem(DEFAULT_COUNTY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].tobaccoUseIndicator").value(hasItem(DEFAULT_TOBACCO_USE_INDICATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].substanceAbuseIndicator").value(hasItem(DEFAULT_SUBSTANCE_ABUSE_INDICATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].lastUpdateDatetime").value(hasItem(DEFAULT_LAST_UPDATE_DATETIME.toString())));
    }
}
