# Chat 01: SC2002 restaurant project stage 1

- **Tool:** Claude (claude.ai, attached to the SC2002 project with the brief and lecture slides)
- **Dates:** Tue 20 Oct 2026 15:03 to Thu 29 Oct 2026 (SGT)
- **Original chat:** https://claude.ai/chat/328f5934-5f5e-474d-955f-404789a4b35b
- **Stage:** 1 (the last part also plans Stage 2)

A selection of the chat, not the full transcript. My prompts are copied word for word;
Claude's replies are summarised in my own words.

---

## 1. Me (20 Oct, 15:03)

> Hi thambi, we want to start working on our SC2002 group project, which is the assignment on the restaurant. so let us following the rubric they want the thing done, now one thing to note is that they r very particular about design principles. so the thing here is that pls refer to the slides and break down the shit for us. this is a sample grp report of my friends from last year but this is on a different topic. so rmb, the rubric goes stage wise so we will also go stage wise. we are 5 ppl in a group; raghav, abhishek, aaryan, aekas and ayush. So what do we do for stage 1. also help for uml and all the shit there too

**Claude:** Read the brief, the sample report and the design-principles and UML slides, then wrote a
Stage 1 plan for the group:
- the Stage 1 rules
- a boundary/control/entity class breakdown, with SOLID mapped to specific classes
- the turn order
- draft PlantUML class and sequence diagrams
- open design decisions
- a 5-person work split
- simulated victory and defeat runs

## 2. Me (20 Oct, 15:51)

> What is the easiest portion inside this man

**Claude:** Ranked the five parts by difficulty. Menu/orders/kitchen was the most self-contained,
balanced by the sequence diagram; controller and integration was the hardest.

## 3. Me (20 Oct)

> Yea give me the lightest part, so make the swap pls

**Claude:** Swapped me onto `Menu`, `MenuItem`, `Order`, `OrderStatus`, `Chef` and `Money` plus the
sequence diagram, moved Aaryan to controller and integration, and updated the plan doc and
review pairs.

## 4. Me (27 Oct, 20:42)

> I want u to follow my simplistic style. it doesnt have to be the most efficient code, but rather i want simple design and thinking ideas

**Claude:** Rewrote my five classes with plain fields, a constructor, getters and simple `if`
checks (no static factory, list copying or read-only views), re-ran the tests, and told me which
line in `Restaurant.java` Aaryan needed to change as a result.

## 5. Me (29 Oct)

> Ok am I right to assume that stage 1 has been completed?

**Claude:** Checked GitHub and listed each Stage 1 deliverable:
- Done: the game following the brief, the victory/defeat/invalid-input runs, the README, the Stage 1 class diagram.
- Waiting on my push: the `stage-1` tag on the right commit.
- Still open: AI histories.

## 6. Me (29 Oct)

> Ok now gimme the document for stage 2, how to split the work and the layout. Take into account all of the information u have

**Claude:** Simulated the Stage 2 rules first, then wrote the Stage 2 plan:
- the Critic always orders the most expensive dish
- turn 4 is the VIP and turn 10 the Critic
- VIP bill = `price * 90 / 100`
- the expected victory and defeat runs, a code sketch, and the work split

My Stage 2 parts were the VIP sequence diagram and the `stage-2` tag.

---

**What I took from this chat:** stage-by-stage planning against the brief, why money is stored as
integer cents, encapsulation through private fields and state-guard methods, and keeping my code
simple enough to explain in the video.
