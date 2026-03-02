```mermaid
game {
   int game_id PK "Primary Key"
   String external_key "Unique Key, not-null"
   String pool "Code pool, non-null"
   int length "Code length, non-null"
   Instant started "Date-time started, non-null"
   boolean solved "Solved flag, not-null"
   Instant last_played "Date-time-of last guess"
   int exact_matches "Exact matches in last guess"
   int near matches "Near matches in last guess"
}
```