package com.ironhack.torneo_germans_bisbal_api.controller;

import com.ironhack.torneo_germans_bisbal_api.model.entity.Rule;
import com.ironhack.torneo_germans_bisbal_api.service.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/rules")
@RequiredArgsConstructor
public class RuleController {
    private final RuleService ruleService;

    @GetMapping
    public List<Rule> getAllRules(){
        return ruleService.getAllRules();
    }

    @GetMapping("/{id}")
    public Rule getRuleById(@PathVariable Long id){
        return ruleService.getRuleById(id);
    }

    @PostMapping
    public Rule createRule(@RequestBody Rule rule){
        return ruleService.createRule(rule);
    }

    @PutMapping("/{id}")
    public Rule updateRule(@PathVariable Long id, @RequestBody Rule rule){
        return ruleService.updateRule(id, rule);
    }

    @DeleteMapping("/{id}")
    public void deleteRule(@PathVariable Long id){
        ruleService.deleteRule(id);
    }
}
