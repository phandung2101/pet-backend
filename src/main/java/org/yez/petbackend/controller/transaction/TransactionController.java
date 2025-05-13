package org.yez.petbackend.controller.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.yez.petbackend.security.PetUser;
import org.yez.petbackend.service.transaction.TransactionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/groups/{groupId}/transactions")
@RequiredArgsConstructor
class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    @PreAuthorize("@groupAuthorizer.isMember(#groupId)")
    @ResponseStatus(HttpStatus.CREATED)
    void create(
            @PathVariable UUID groupId,
            @AuthenticationPrincipal PetUser user,
            @RequestBody CreateTransactionRequest createTransactionRequest
    ) {
        transactionService.create(groupId, user.getId(), createTransactionRequest);
    }

    @GetMapping
    @PreAuthorize("@groupAuthorizer.isMember(#groupId)")
    List<TransactionResponse> findAll(
            @PathVariable UUID groupId
    ) {
        return transactionService.findAll(groupId).stream()
                .map(TransactionResponse::new)
                .toList();
    }

    @GetMapping("{transactionId}")
    @PreAuthorize("@groupAuthorizer.isMember(#groupId)")
    TransactionResponse findOne(
            @PathVariable UUID groupId,
            @PathVariable UUID transactionId
    ) {
        return Optional.of(transactionService.findOne(groupId, transactionId))
                .map(TransactionResponse::new)
                .get();
    }

    @PatchMapping("{transactionId}")
    @PreAuthorize("@groupAuthorizer.isMember(#groupId)")
    void update(
            @PathVariable UUID groupId,
            @PathVariable UUID transactionId,
            @RequestBody UpdateTransactionRequest request
    ) {
        transactionService.update(groupId, transactionId, request);
    }

    @DeleteMapping("{transactionId}")
    @PreAuthorize("@groupAuthorizer.isMember(#groupId)")
    void delete(
            @AuthenticationPrincipal PetUser user,
            @PathVariable UUID groupId,
            @PathVariable UUID transactionId
    ) {
        transactionService.delete(groupId, user.getId(), transactionId);
    }

}
