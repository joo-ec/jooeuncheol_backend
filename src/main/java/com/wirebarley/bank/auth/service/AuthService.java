package com.wirebarley.bank.auth.service;

import com.wirebarley.bank.common.dto.request.AuthRequest;
import com.wirebarley.bank.common.dto.request.JoinRequest;

public interface AuthService {
    String login(AuthRequest authRequest);

    void join(JoinRequest joinRequest);
}
