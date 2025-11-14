export interface User {
  id: string;
  username: string;
  email: string;
  displayName: string;
  roles: string[];
}

export interface ProblemSummary {
  id: string;
  slug: string;
  title: string;
  difficulty: 'EASY' | 'MEDIUM' | 'HARD';
  tags: string | null;
  updatedAt: string;
}

export interface Testcase {
  id: string;
  visible: boolean;
  score: number;
}

export interface ProblemDetail extends ProblemSummary {
  statement: string;
  inputSpecification: string | null;
  outputSpecification: string | null;
  sampleInput: string | null;
  sampleOutput: string | null;
  timeLimitMs: number;
  memoryLimitMb: number;
  testcases: Testcase[];
}

export interface Submission {
  id: string;
  problemId: string;
  languageId: string;
  status: 'QUEUED' | 'RUNNING' | 'COMPLETED' | 'ERRORED';
  verdict:
    | 'ACCEPTED'
    | 'WRONG_ANSWER'
    | 'TIME_LIMIT_EXCEEDED'
    | 'MEMORY_LIMIT_EXCEEDED'
    | 'RUNTIME_ERROR'
    | 'COMPILATION_ERROR'
    | 'INTERNAL_ERROR';
  score: number;
  createdAt: string;
}

export interface SubmissionDetail extends Submission {
  language: string;
  code: string;
  testcaseResults: Array<{
    testcaseId: string;
    verdict: Submission['verdict'];
    timeMs: number | null;
    memoryKb: number | null;
    message: string | null;
  }>;
}

export interface ProgrammingLanguage {
  id: string;
  name: string;
  version: string;
  enableSubmission: boolean;
}

