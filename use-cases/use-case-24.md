# USE CASE: 24 Produce a Report on All Capital-Cities in a Continent

## CHARACTERISTIC INFORMATION

### Goal in Context

As a User i want to list All the capital cities in a continent organised by largest population to smallest so that I can support population reporting of the organization.

### Scope

Company

### Preconditions

Database contains city and country data.

### Success End Condition

A report of all capital-cities in a specified continent is produced.

### Failed End Condition

No report is produced.

### Primary Actor

User

### Trigger

A request for population information is sent to the user

## MAIN SUCCESS SCENARIO

1. Request is made for information on all capital-cities
2. User inputs continent name.
3. User extracts information on capital-cities.
4. User provides report to requester.

## EXTENSIONS

3. **Continent does not exist:**
    1. User informs requester that continent given does not exist in database.