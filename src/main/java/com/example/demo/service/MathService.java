package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MathService {
    public static String getSumString(List<String> params){
        int sum = 0;
        String result = "";
        for (int i = 0; i < params.size(); i++) {
            sum += Integer.valueOf(params.get(i));

            if(i < params.size() -1) {
                result += (params.get(i) + " + ");

            } else {
                result += (params.get(i) + " = " + sum);
            }
        }
        return result;
    }
}

