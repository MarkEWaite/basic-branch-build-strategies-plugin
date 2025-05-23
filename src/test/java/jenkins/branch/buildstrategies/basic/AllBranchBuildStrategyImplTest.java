/*
 * The MIT License
 *
 * Copyright (c) 2018-2019, Bodybuilding.com, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jenkins.branch.buildstrategies.basic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.model.TaskListener;
import java.util.Arrays;
import java.util.Collections;
import jenkins.branch.BranchBuildStrategy;
import jenkins.scm.api.SCMHead;
import jenkins.scm.api.SCMRevision;
import jenkins.scm.api.SCMSource;
import jenkins.scm.impl.mock.MockSCMController;
import jenkins.scm.impl.mock.MockSCMHead;
import jenkins.scm.impl.mock.MockSCMRevision;
import jenkins.scm.impl.mock.MockSCMSource;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class AllBranchBuildStrategyImplTest {

    @Test
    void given__no_strategies__when__isAutomaticBuild__then__returns_false() {
        try (MockSCMController c = MockSCMController.create()) {
            MockSCMHead head = new MockSCMHead("master");
            assertThat(
                    new AllBranchBuildStrategyImpl(Collections.emptyList())
                            .isAutomaticBuild(
                                    new MockSCMSource(c, "dummy"),
                                    head,
                                    new MockSCMRevision(head, "dummy"),
                                    null,
                                    null,
                                    null),
                    is(false));
        }
    }

    @Test
    void given__true_strategies__when__isAutomaticBuild__then__returns_true() {
        try (MockSCMController c = MockSCMController.create()) {
            MockSCMHead head = new MockSCMHead("master");
            assertThat(
                    new AllBranchBuildStrategyImpl(Arrays.asList(
                                    new BranchBuildStrategy() {
                                        @Override
                                        public boolean isAutomaticBuild(
                                                @NonNull SCMSource source,
                                                @NonNull SCMHead head,
                                                @NonNull SCMRevision currRevision,
                                                SCMRevision lastBuiltRevision,
                                                SCMRevision lastSeenRevision,
                                                @NonNull TaskListener taskListener) {
                                            return true;
                                        }
                                    },
                                    new BranchBuildStrategy() {
                                        @Override
                                        public boolean isAutomaticBuild(
                                                @NonNull SCMSource source,
                                                @NonNull SCMHead head,
                                                @NonNull SCMRevision currRevision,
                                                SCMRevision lastBuiltRevision,
                                                SCMRevision lastSeenRevision,
                                                @NonNull TaskListener taskListener) {
                                            return true;
                                        }
                                    }))
                            .isAutomaticBuild(
                                    new MockSCMSource(c, "dummy"),
                                    head,
                                    new MockSCMRevision(head, "dummy"),
                                    null,
                                    null,
                                    null),
                    is(true));
        }
    }

    @Test
    void given__false_strategies__when__isAutomaticBuild__then__returns_false() {
        try (MockSCMController c = MockSCMController.create()) {
            MockSCMHead head = new MockSCMHead("master");
            assertThat(
                    new AllBranchBuildStrategyImpl(Arrays.asList(
                                    new BranchBuildStrategy() {
                                        @Override
                                        public boolean isAutomaticBuild(
                                                @NonNull SCMSource source,
                                                @NonNull SCMHead head,
                                                @NonNull SCMRevision currRevision,
                                                SCMRevision lastBuiltRevision,
                                                SCMRevision lastSeenRevision,
                                                @NonNull TaskListener taskListener) {
                                            return false;
                                        }
                                    },
                                    new BranchBuildStrategy() {
                                        @Override
                                        public boolean isAutomaticBuild(
                                                @NonNull SCMSource source,
                                                @NonNull SCMHead head,
                                                @NonNull SCMRevision currRevision,
                                                SCMRevision lastBuiltRevision,
                                                SCMRevision lastSeenRevision,
                                                @NonNull TaskListener taskListener) {
                                            return false;
                                        }
                                    }))
                            .isAutomaticBuild(
                                    new MockSCMSource(c, "dummy"),
                                    head,
                                    new MockSCMRevision(head, "dummy"),
                                    null,
                                    null,
                                    null),
                    is(false));
        }
    }

    @Test
    void given__false_and_true_strategies__when__isAutomaticBuild__then__returns_false_with_short_circuit() {
        try (MockSCMController c = MockSCMController.create()) {
            MockSCMHead head = new MockSCMHead("master");
            assertThat(
                    new AllBranchBuildStrategyImpl(Arrays.asList(
                                    new BranchBuildStrategy() {
                                        @Override
                                        public boolean isAutomaticBuild(
                                                @NonNull SCMSource source,
                                                @NonNull SCMHead head,
                                                @NonNull SCMRevision currRevision,
                                                SCMRevision lastBuiltRevision,
                                                SCMRevision lastSeenRevision,
                                                @NonNull TaskListener taskListener) {
                                            return false;
                                        }
                                    },
                                    new BranchBuildStrategy() {
                                        @Override
                                        public boolean isAutomaticBuild(
                                                @NonNull SCMSource source,
                                                @NonNull SCMHead head,
                                                @NonNull SCMRevision currRevision,
                                                SCMRevision lastBuiltRevision,
                                                SCMRevision lastSeenRevision,
                                                @NonNull TaskListener taskListener) {
                                            return true;
                                        }
                                    }))
                            .isAutomaticBuild(
                                    new MockSCMSource(c, "dummy"),
                                    head,
                                    new MockSCMRevision(head, "dummy"),
                                    null,
                                    null,
                                    null),
                    is(false));
        }
    }

    @Test
    void equalsContract() {
        EqualsVerifier.forClass(AllBranchBuildStrategyImpl.class)
                .usingGetClass()
                .verify();
    }
}
