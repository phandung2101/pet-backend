package org.yez.petbackend.controller.token;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.yez.petbackend.domain.token.Token;
import org.yez.petbackend.security.PetUser;
import org.yez.petbackend.service.token.TokenService;

import java.util.List;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> createToken(@AuthenticationPrincipal PetUser user, @RequestBody CreateTokenRequest request) {
        Token token = tokenService.createToken(user.getId(), request.name(), request.expiresAt());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @GetMapping
    public ResponseEntity<List<TokenResponse>> getTokens(@AuthenticationPrincipal PetUser user) {
        List<Token> tokens = tokenService.getTokensByUserId(user.getId());
        List<TokenResponse> response = tokens.stream()
                .map(TokenResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<Void> deleteToken(@PathVariable String token, @AuthenticationPrincipal PetUser user) {
        // Verify the token belongs to the user before deleting
        List<Token> userTokens = tokenService.getTokensByUserId(user.getId());
        boolean tokenBelongsToUser = userTokens.stream()
                .anyMatch(t -> t.token().equals(token));
        
        if (!tokenBelongsToUser) {
            return ResponseEntity.notFound().build();
        }
        
        tokenService.deleteToken(token);
        return ResponseEntity.noContent().build();
    }
}
