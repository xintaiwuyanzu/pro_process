package com.dr.framework.core.orm.support.liquibase;

import liquibase.change.Change;
import liquibase.change.core.DropIndexChange;
import liquibase.change.core.DropTableChange;
import liquibase.change.core.DropViewChange;
import liquibase.changelog.ChangeSet;
import liquibase.diff.DiffResult;
import liquibase.diff.output.DiffOutputControl;
import liquibase.diff.output.changelog.DiffToChangeLog;

import java.util.List;
import java.util.stream.Collectors;

public class DiffToChangeLogWithoutDrop extends DiffToChangeLog {
    public DiffToChangeLogWithoutDrop(DiffResult diffResult, DiffOutputControl diffOutputControl) {
        super(diffResult, diffOutputControl);
    }

    @Override
    public List<ChangeSet> generateChangeSets() {
        List<ChangeSet> changeSets = super.generateChangeSets();
        return changeSets.stream().filter(changeSet -> {
            if (changeSet.getChanges() != null && changeSet.getChanges().size() == 1) {
                Change change = changeSet.getChanges().get(0);
                if (change instanceof DropTableChange || change instanceof DropIndexChange || change instanceof DropViewChange) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
    }
}
