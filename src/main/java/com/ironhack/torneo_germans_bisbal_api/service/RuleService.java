package com.ironhack.torneo_germans_bisbal_api.service;

import com.ironhack.torneo_germans_bisbal_api.model.entity.Rule;

import java.util.List;

public interface RuleService {

    List<Rule> getAllRules();

    Rule getRuleById(Long id);

    Rule createRule(Rule rule);

    Rule updateRule(Long id, Rule rule);

    void deleteRule(Long id);
}