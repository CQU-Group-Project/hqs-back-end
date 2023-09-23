package com.cqu.hqs.service;

import com.cqu.hqs.Repository.GuestRepository;
import com.cqu.hqs.Repository.QueryRepository;
import com.cqu.hqs.dto.QueryDto;
import com.cqu.hqs.dto.QueryResponseDto;
import com.cqu.hqs.entity.Employee;
import com.cqu.hqs.entity.Query;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class QueryService {

    private ModelMapper mapper;
    private QueryRepository queryRepository;

    public QueryService(ModelMapper modelMapper, QueryRepository queryRepository) {
        this.mapper = modelMapper;
        this.queryRepository = queryRepository;
    }

    @Transactional
    public Object saveQuery(QueryDto queryDto) {
        Query query = mapToEntity(queryDto);
        query.setCreatedDate(LocalDateTime.now());
        query = queryRepository.save(query);
        return mapToResponseDto(query);
    }

    public List<QueryResponseDto> getAllQueries() {
        List<Query> queries = queryRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        return queries.stream().map(q -> mapToResponseDto(q)).collect((Collectors.toList()));
    }

    private Query mapToEntity(QueryDto queryDto) {
        Query q = mapper.map(queryDto, Query.class);
        return q;
    }

    public QueryResponseDto mapToResponseDto(Query q) {
        QueryResponseDto queryResponseDto = mapper.map(q, QueryResponseDto.class);
        return queryResponseDto;
    }

}
