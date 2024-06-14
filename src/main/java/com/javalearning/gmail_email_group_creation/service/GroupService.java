package com.javalearning.gmail_email_group_creation.service;

import com.google.api.services.admin.directory.Directory;
import com.google.api.services.admin.directory.model.Group;
import com.google.api.services.admin.directory.model.Member;
import com.google.api.services.admin.directory.model.Members;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {

    @Autowired
    private Directory directoryService;

    public Group createOrUpdateGroup(String email, String name, String description) throws IOException {
        Group group;
        try {
            group = directoryService.groups().get(email).execute();
            group.setName(name);
            group.setDescription(description);
            group = directoryService.groups().update(email, group).execute();
        } catch (Exception e) {
            group = new Group()
                    .setEmail(email)
                    .setName(name)
                    .setDescription(description);
            group = directoryService.groups().insert(group).execute();
        }
        return group;
    }

    public List<Member> addMembersToGroup(String groupId, List<String> emails) throws IOException {
        List<Member> addedMembers = new ArrayList<>();
        Members existingMembers = directoryService.members().list(groupId).execute();
        List<String> existingEmails = new ArrayList<>();
        if (existingMembers.getMembers() != null) {
            for (Member member : existingMembers.getMembers()) {
                existingEmails.add(member.getEmail());
            }
        }
        for (String email : emails) {
            if (!existingEmails.contains(email)) {
                Member member = new Member().setEmail(email).setRole("MEMBER");
                addedMembers.add(directoryService.members().insert(groupId, member).execute());
            }
        }
        return addedMembers;
    }

    public void removeAllMembersFromGroup(String groupId) throws IOException {
        Members members = directoryService.members().list(groupId).execute();
        if (members.getMembers() != null) {
            for (Member member : members.getMembers()) {
                directoryService.members().delete(groupId, member.getId()).execute();
            }
        }
    }

    public void removeMemberFromGroup(String groupId, String memberId) throws IOException {
        directoryService.members().delete(groupId, memberId).execute();
    }
}
