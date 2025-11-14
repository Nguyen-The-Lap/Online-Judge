CREATE TABLE app_user (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(128) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(100) NOT NULL,
    roles VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE user_roles (
    user_id UUID NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role)
);

CREATE TABLE programming_language (
    id UUID PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE,
    version VARCHAR(32),
    compile_command VARCHAR(255),
    run_command VARCHAR(255),
    enable_submission BOOLEAN DEFAULT TRUE
);

CREATE TABLE problem (
    id UUID PRIMARY KEY,
    slug VARCHAR(80) NOT NULL UNIQUE,
    title VARCHAR(150) NOT NULL,
    difficulty VARCHAR(20) NOT NULL,
    tags TEXT,
    statement TEXT NOT NULL,
    input_spec TEXT,
    output_spec TEXT,
    sample_input TEXT,
    sample_output TEXT,
    time_limit_ms INTEGER NOT NULL DEFAULT 2000,
    memory_limit_mb INTEGER NOT NULL DEFAULT 256,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    author_id UUID NOT NULL REFERENCES app_user(id)
);

CREATE TABLE problem_testcase (
    id UUID PRIMARY KEY,
    problem_id UUID NOT NULL REFERENCES problem(id) ON DELETE CASCADE,
    input_blob TEXT NOT NULL,
    output_blob TEXT NOT NULL,
    visible BOOLEAN DEFAULT FALSE,
    score INTEGER NOT NULL DEFAULT 100
);

CREATE TABLE submission (
    id UUID PRIMARY KEY,
    problem_id UUID NOT NULL REFERENCES problem(id),
    user_id UUID NOT NULL REFERENCES app_user(id),
    language_id UUID NOT NULL REFERENCES programming_language(id),
    status VARCHAR(40) NOT NULL,
    score INTEGER DEFAULT 0,
    verdict VARCHAR(40) NOT NULL,
    code TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE submission_testcase_result (
    id UUID PRIMARY KEY,
    submission_id UUID NOT NULL REFERENCES submission(id) ON DELETE CASCADE,
    testcase_id UUID NOT NULL REFERENCES problem_testcase(id) ON DELETE CASCADE,
    time_ms INTEGER,
    memory_kb INTEGER,
    verdict VARCHAR(40) NOT NULL,
    message TEXT
);

