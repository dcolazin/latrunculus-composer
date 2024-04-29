package org.vetronauta.latrunculus.plugin.base;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NoopPlugin implements Plugin {

    public static final NoopPlugin INSTANCE = new NoopPlugin();

    @Override
    public void run(RunInfo runInfo) {
        //noop
    }

}
