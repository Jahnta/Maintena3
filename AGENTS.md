# Maintena3

- This is the Git root for `maintena-android` and `maintena-server`; never create nested repositories.
- Work from the affected subproject and use its Gradle wrapper. Build files and version catalogs are authoritative; do not edit generated `.idea`, `.gradle`, `build`, or `.kotlin` state.
- Never commit machine-local files or secrets, including `local.properties`, credentials, signing keys, and service-account files.
- API-contract changes must update and verify both projects as needed; preserve compatible consumers without gratuitous edits.
- Preserve configured Java/Kotlin toolchains unless changing them intentionally.
- Before finishing, run `git diff --check` and `git status`; report skipped checks or environment limits.
- When a change alters project layout, toolchains, canonical checks, or these invariants, update the nearest `AGENTS.md` in the same change. Keep guidance durable and do not repeat parent rules.

## Development discipline

- Inspect the existing implementation and working-tree changes first. Preserve unrelated and user-owned changes.
- Prefer the smallest coherent change, existing code style, libraries, and abstractions. Do not perform unrelated refactoring, duplicate abstractions, or add dependencies when the current stack suffices.
- Preserve backward compatibility unless the requested behavior requires a break; identify the impact and coordinate a migration before implementation.
- Apply `maintena-android/AGENTS.md` and/or `maintena-server/AGENTS.md` before work in those trees, including from root; read them only if not already in context. More specific instructions govern their subtree.

## Choose the smallest workflow

- Classify by risk and dependencies before delegating, not file count alone. Use the fewest agents that materially help; an available role is not a required stage.
- SMALL: obvious local UI/bug/rename/function/query/formatting changes. Main implements and validates; no subagents by default, including architect/reviewer. A compatibility or data-loss risk overrides the size label.
- MEDIUM: a bounded single-platform feature or moderate refactor. Main works alone or uses at most one specialist total (exploration, implementation, or risk review). Main reviews delegated implementation; do not chain specialists for ceremony.
- LARGE: coordinated Android/server features, new API design, migrations, sync/concurrency, auth/security, significant domain changes, or unclear cross-layer dependencies. Delegate only necessary stages: architect when design is unresolved -> implementation -> contract review when applicable -> independent reviewer -> validation. Reclassify risky work instead of silently exceeding MEDIUM's budget.
- Use explorer only for an unknown implementation path. Skip it for named files, local tasks, or paths already mapped by main/architect. Architect is for substantive decisions in LARGE work, never routine CRUD/UI.
- Use contract_reviewer only for changed client/server boundaries: endpoint/method/parameters, DTOs, serialization, enums/nullability, UUID/date formats, errors, pagination, or compatibility. For a straightforward MEDIUM boundary change, main implements and this can be the sole specialist.
- Use reviewer for LARGE or risky changes (migration, concurrency, offline-first, auth/security, major refactor, API risk). SMALL needs targeted validation, not a separate reviewer. Do not repeat a completed contract review; focus final review on remaining risks and integration.

## Coordination and context

- Main owns requirements, decisions, one API contract, integration, and results. Settle endpoint/method/parameters, payloads, errors, serialization, compatibility/migration and acceptance checks before client/server implementation. Internal agreement does not require user approval.
- Give each agent a narrow goal, file/symbol map, current flow, relevant contract/decisions, disjoint owned files, and expected output. Prefer a fresh context with that brief (no full-history fork when unnecessary). Reuse findings; inspect relevant source to implement/review, but repeat exploration only for missing, stale, or contradictory evidence.
- Parallelize android_developer and server_developer only for independent work under the agreed contract. Never overlap writers, including main. Main owns root/shared files and coordinates scope or contract revisions before work resumes.
- Only main delegates. Close/reuse completed agents; at most two subagents may be open concurrently. Subagents return concise findings (normally <=200 words): paths/symbols, flow or changes, checks/results, risks. Omit empty sections, file summaries, large code excerpts, and long reasoning; include all actionable findings even if longer.
- Keep durable knowledge in AGENTS.md, architecture/API docs, and source. Prefer a new task for an independent feature when old history is unnecessary; recommend it rather than creating a task without the user's request.

## Validation and model budget

- Main is the final validation owner and assigns any worker checks explicitly. Update tests for changed behavior. Start with a relevant test/class, then module tests/check/build; run full-project checks only for broad impact. Reuse passing results for unchanged inputs; rerun only after relevant edits, failures, or unresolved risks. Do not have multiple agents run the same Gradle tasks.
- Main, architect, developers and reviewer default to GPT-5.6 Terra / medium; explorer uses Luna / low and contract reviewer Luna / medium. Never select Astra for exploration, CRUD, DTO mapping, ordinary Compose/endpoints/Repository/DAO changes, or routine review. SMALL/MEDIUM work stays on the balanced defaults.
- Escalate to `gpt-6-astra` only for genuinely difficult architecture, nontrivial Android/backend dependencies, offline sync, concurrency/races, complex migrations, security/auth reasoning, or hard systemic diagnosis. A LARGE label or touching two projects alone is insufficient. State the unresolved question and why stronger cross-system reasoning is needed; scope Astra to that question, then resume balanced implementation/review.
- For a justified architect escalation, explicitly pass model `gpt-6-astra` and reasoning `medium` at spawn; use `high` only if the specific difficulty warrants it. Architect does not pin model/effort, so spawn overrides work; otherwise it inherits Terra/medium. Role-file values take precedence over spawn settings: explorer pins Luna/low, contract reviewer pins Luna. Do not change project defaults to handle one hard task.
- Main's project default is Terra/medium. Explicit app/CLI model choices can override it; TOML does not classify tasks and switch the running main agent automatically. If a main-session switch is needed, use the supported model selector (or recommend it when unavailable), never claim that editing config switched the current turn. Do not spawn extra agents just to work around the main model selection.

## Agent configuration and checks

- `.codex/config.toml` registers six role files in `.codex/agents/`. Read-only: architect, explorer, contract_reviewer, reviewer. Writers: android_developer, server_developer, within their assigned subproject files.
- Role sandbox settings are defaults, not a guarantee against live parent permission overrides. Read-only agents must not edit files or run Gradle (which writes caches/output), even in a full-access session. Writer subtree ownership is an orchestration rule, not a filesystem ACL.
- Start a fresh session in this trusted repository after changing agent configuration. If named roles are unavailable, report that limitation; do not claim a generic agent has loaded a custom role or its sandbox.
- For configuration-only changes, check TOML with the installed Codex strict parser, confirm role discovery and instruction hierarchy, and run the Git checks above; application builds are unnecessary if application/build inputs did not change.
