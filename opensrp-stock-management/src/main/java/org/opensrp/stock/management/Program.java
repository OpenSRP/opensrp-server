package org.opensrp.stock.management;

import java.util.UUID;

public class Program extends BaseEntity {

    Code code;
    String name;
    String description;
    Boolean active;
    Boolean periodsSkippable;
    Boolean skipAuthorization;
    Boolean showNonFullSupplyTab;
    Boolean enableDatePhysicalStockCountCompleted;

    public Program() {
        code = null;
    }

    public Program(UUID id) {
        this.id = id;
    }

    public Program(String programCode) {
        this.code = Code.code(programCode);
    }

    /**
     * Equal by a Program's code.
     *
     * @param other the other Program
     * @return true if the two Program's {@link Code} are equal.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Program)) {
            return false;
        }

        Program otherProgram = (Program) other;
        return code.equals(otherProgram.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}


