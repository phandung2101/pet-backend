package org.yez.petbackend.controller.group;

import java.util.List;

record GroupResponse(
        String id,
        String name,
        String description,
        List<String> members,
        String owner
) {

}
