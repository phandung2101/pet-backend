package org.yez.petbackend.service.transaction;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.yez.petbackend.domain.transaction.Transaction;
import org.yez.petbackend.domain.transaction.TransactionMapper;
import org.yez.petbackend.repository.transaction.TransactionRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
class TransactionQueryImpl implements TransactionQuery {
    private final TransactionRepository transactionRepository;

    @Override
    public Transaction findOne(final UUID groupId, final UUID transactionId) {
        return transactionRepository.findByGroup_IdAndId(groupId, transactionId)
                .map(TransactionMapper::mapFromEntity)
                .orElseThrow(TransactionNotExistedException::new);
    }

    @Override
    public List<Transaction> findAll(final UUID groupId) {
        return transactionRepository.findAllByGroup_Id(groupId)
                .stream().map(TransactionMapper::mapFromEntity)
                .toList();
    }
}
