package com.ironhack.torneo_germans_bisbal_api.service.impl;

import com.ironhack.torneo_germans_bisbal_api.exception.ResourceNotFoundException;
import com.ironhack.torneo_germans_bisbal_api.model.entity.Rule;
import com.ironhack.torneo_germans_bisbal_api.repository.RuleRepository;
import com.ironhack.torneo_germans_bisbal_api.service.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ruleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;


    @Override
    public List<Rule> getAllRules() {
        return ruleRepository.findAll();
    }
    @Override
    public Rule getRuleById(Long id) {
        return ruleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rule not found with id: " + id));
    }

    @Override
    public Rule createRule(Rule rule) {
        return ruleRepository.save(rule);
    }

    @Override
    public Rule updateRule(Long id, Rule rule) {
        Rule existingRule = getRuleById(id);
        existingRule.setTitle(rule.getTitle());
        existingRule.setDescription(rule.getDescription());
        return ruleRepository.save(existingRule);
    }

    @Override
    public void deleteRule(Long id) {
        getRuleById(id);
        ruleRepository.deleteById(id);
    }
}
