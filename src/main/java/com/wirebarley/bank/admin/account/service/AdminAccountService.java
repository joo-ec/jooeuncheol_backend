package com.wirebarley.bank.admin.account.service;

import com.wirebarley.bank.common.dto.request.ApprovalAccountRequest;

public interface AdminAccountService {
    void approvalAccount(ApprovalAccountRequest accountRequest);
}
