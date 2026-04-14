package com.payment.models;

import java.util.Set;

public enum PixStatus {

    PENDING {
        @Override
        public Set<PixStatus> allowedTransitions() {
            return Set.of(PROCESSING, REJECTED);
        }
    },
    PROCESSING {
        @Override
        public Set<PixStatus> allowedTransitions() {
            return Set.of(SETTLED, REJECTED);
        }
    },
    SETTLED {
        @Override
        public Set<PixStatus> allowedTransitions() {
            return Set.of();
        }
    },
    REJECTED {
        @Override
        public Set<PixStatus> allowedTransitions() {
            return Set.of();
        }
    };

    public abstract Set<PixStatus> allowedTransitions();

    public boolean canTransitionTo(PixStatus target) {
        return allowedTransitions().contains(target);
    }
}
