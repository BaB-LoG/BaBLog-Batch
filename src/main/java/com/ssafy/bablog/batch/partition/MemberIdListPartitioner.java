package com.ssafy.bablog.batch.partition;

import com.ssafy.bablog.batch.member.MemberIdProvider;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberIdListPartitioner implements Partitioner {

    private final MemberIdProvider memberIdProvider;

    public MemberIdListPartitioner(MemberIdProvider memberIdProvider) {
        this.memberIdProvider = memberIdProvider;
    }

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        List<Long> memberIds = memberIdProvider.fetchAllMemberIds();
        Map<String, ExecutionContext> partitions = new HashMap<>();
        if (memberIds == null || memberIds.isEmpty()) {
            return partitions;
        }

        int safeGridSize = Math.max(1, gridSize);
        int chunkSize = (int) Math.ceil(memberIds.size() / (double) safeGridSize);
        int partitionIndex = 0;
        for (int start = 0; start < memberIds.size(); start += chunkSize) {
            int end = Math.min(start + chunkSize, memberIds.size());
            List<Long> slice = new ArrayList<>(memberIds.subList(start, end));
            ExecutionContext context = new ExecutionContext();
            context.put("memberIds", slice);
            partitions.put("partition" + partitionIndex, context);
            partitionIndex++;
        }
        return partitions;
    }
}
