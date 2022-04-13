package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * @author Silvio Giebl
 */
class ParseContext {

    private int nextGroupIndex = 0;
    private @Nullable HashMap<@NotNull String, @NotNull GroupEntry> groupEntries;

    private static class GroupEntry {
        final int index;
        final @NotNull LinkedList<Group> groups = new LinkedList<>();
        final @NotNull LinkedList<Reference> references = new LinkedList<>();

        GroupEntry(final int index) {
            this.index = index;
        }
    }

    @NotNull Group addGroup(final @NotNull String name, final @NotNull RegexPart part) {
        final GroupEntry groupEntry = getGroupEntry(name);
        final Group group = new Group(groupEntry.index, name, part);
        groupEntry.groups.add(group);
        return group;
    }

    @NotNull Reference addReference(final @NotNull String name) {
        final GroupEntry groupEntry = getGroupEntry(name);
        final Reference reference = new Reference(groupEntry.index, name);
        groupEntry.references.add(reference);
        return reference;
    }

    private @NotNull GroupEntry getGroupEntry(final @NotNull String name) {
        if (groupEntries == null) {
            groupEntries = new HashMap<>();
        }
        GroupEntry groupEntry = groupEntries.get(name);
        if (groupEntry == null) {
            groupEntry = new GroupEntry(nextGroupIndex++);
            groupEntries.put(name, groupEntry);
        }
        return groupEntry;
    }

    void validate() {
        if (groupEntries == null) {
            return;
        }
        groupEntries.forEach((name, groupEntry) -> {
            if (groupEntry.groups.isEmpty()) {
                throw new IllegalArgumentException("group '" + name + "' is missing but is referenced"); // TODO message
            }
        });
    }
}
