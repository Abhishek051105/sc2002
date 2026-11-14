# AI Interaction History

### Raghav

My project-related AI chats for Stage 2. The file contains a selection of my prompts (copied word for word) with Copilot's replies summarised, not the full transcript.

The discussions focused on my assigned Stage 2 responsibilities:

- Customer type abstraction (`getTypeLabel()`)
- VIPCustomer implementation
- Open-Closed Principle compliance
- Customer hierarchy extensibility
- Polymorphism-based customer identification
- Stage 2 UML class diagram updates
- Git workflow and branch integration planning
- AI evidence and documentation requirements

| File | Dates (SGT) | What it covers |
|---|---|---|
| [`Stage2_AI_History.md`](Stage2_AI_History.md) | 31 Oct 2026 | Stage 2 requirements analysis, VIPCustomer design, customer-type abstraction through polymorphism, Open-Closed Principle considerations, UML inheritance updates, implementation planning, Git workflow guidance, and design decisions for extending the customer model without modifying existing controller logic. |

## Key Learning Points

Through these discussions, I learned:

- How the Open-Closed Principle can be applied by extending behaviour through inheritance instead of modifying existing controller code.
- Why polymorphism is preferred over repeated `instanceof` checks when handling different customer types.
- How a common abstraction can simplify future customer extensions.
- How customer-specific behaviour should be encapsulated within subclasses while preserving shared functionality in the parent class.
- How UML diagrams can be used to communicate inheritance structures and design rationale.
- How to introduce new functionality while minimizing changes to stable components of the system.
- How feature branches and pull requests support safer team-based software development.
