package com.javalearning.gmail_email_group_creation.controller;

import com.google.api.services.admin.directory.model.Group;
import com.google.api.services.admin.directory.model.Member;
import com.javalearning.gmail_email_group_creation.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
@Tag(name = "Email Group API", description = "API for managing Google email groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    @Operation(summary = "Create or update an email group")
    public Group createOrUpdateGroup(@RequestParam String email, @RequestParam String name, @RequestParam String description) throws IOException {
        return groupService.createOrUpdateGroup(email, name, description);
    }

    @PostMapping("/{groupId}/members")
    @Operation(summary = "Add members to a group")
    public List<Member> addMembersToGroup(@PathVariable String groupId, @RequestBody List<String> emails) throws IOException {
        return groupService.addMembersToGroup(groupId, emails);
    }

    @DeleteMapping("/{groupId}/members")
    @Operation(summary = "Remove all members from a group")
    public void removeAllMembersFromGroup(@PathVariable String groupId) throws IOException {
        groupService.removeAllMembersFromGroup(groupId);
    }

    @DeleteMapping("/{groupId}/members/{memberId}")
    @Operation(summary = "Remove a member from a group")
    public void removeMemberFromGroup(@PathVariable String groupId, @PathVariable String memberId) throws IOException {
        groupService.removeMemberFromGroup(groupId, memberId);
    }
}
