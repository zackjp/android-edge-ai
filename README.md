## Project

This repo is where I experiment with AI on the client side: agentic interactions first,
local GenAI to come.

## Dynamic Agentic UI

### Background

A2UI is a protocol that lets chat agents build native UI components in their responses. Agents
send [JSON Lines](https://jsonlines.org/), and the client renders them. Google covers the Android
side in their [Agent-to-UI](https://developer.android.com/develop/ui/compose/agentic) docs.

Replacing walls of text with native, structured UI makes agentic chats easier to read.
A2UI 1.0-alpha also introduces interactivity, so the UI can send data back to the agent. For
example, an agent could create a "Flight Scheduling" card with a "Book Flight" button that
purchases the ticket.

### Result

Here's the A2UI protocol in action in this project: a native Weather Card rendered from an
agent response. Agents stream their responses, and JSONL lets the UI build up as the data
comes in.

<img height="450" src="https://github.com/user-attachments/assets/4828640f-67e1-41c3-9393-3e90a64c5217" /> <video height="450" src="https://github.com/user-attachments/assets/3799eefc-634a-4849-a18e-95cb9cc809d4" controls></video>

### Approach

The goal was to learn how A2UI works under the hood, so I built it incrementally and without
any AI code generation.

I started with the chat UI and no A2UI at all: a simple "echo" command where a fake agent
repeats the user's message.

From there, I integrated a single A2UI `Surface` to understand what it really is, how it's
rendered, who renders it, when it gets rendered, and how data gets passed in. A fake agent
emitting A2UI JSONL operations simulated the other side. Once I had those answers, I moved on
to multiple `Surface`s. E.g., how do I correlate each one and update it with agent chat messages?

With that figured out, I updated the fake agent to build a Weather Card from A2UI's default
components (Text, Column, etc.). Then I created a custom Weather Card component and updated
the simulated A2UI messages to match.

Next up is swapping in a real agent to see how well A2UI actually performs.

### Learnings

#### Default components

I originally used default components like Text and Column as building blocks for the Weather
Card. They give agents flexibility over what information to show and how to arrange it, but
there are downsides:

- Styling is limited – A2UI doesn't allow custom padding by design, which makes design
  difficult. Text sizes are also limited to about five presets.

- Too much arrangement flexibility – The agent could lay out the weather card differently each
  time. Whether that's a downside depends on how much you value consistency. I do, so I count it.

Here's the Weather Card built with default components. It gets the job done, but lacks
visual identity.

<video height="450" src="https://github.com/user-attachments/assets/1b3ce229-7cd7-44e2-a283-2cc5e11538ea" controls></video>

#### Custom components

My opinion is that custom components are worth it. You get consistent visual design and full
control over spacing and typography. The tradeoff is that new components require a mobile
app release.