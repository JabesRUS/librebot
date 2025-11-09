# Ключевые слова Spring Data JPA для построения запросов

| Ключевое слово | Пример метода | SQL-эквивалент |
|----------------|----------------|----------------|
| **findBy** | `findByChatId(Long id)` | `WHERE chat_id = ?` |
| **And** | `findByChatIdAndLibreMail(String mail)` | `WHERE chat_id = ? AND libre_mail = ?` |
| **Or** | `findByLibreMailOrPatientId(String mail, Long id)` | `WHERE libre_mail = ? OR patient_id = ?` |
| **OrderBy** | `findAllByOrderByChatIdAsc()` | `ORDER BY chat_id ASC` |
| **Like** | `findByLibreMailLike(String pattern)` | `WHERE libre_mail LIKE ?` |
| **IsNull** | `findByLibreTokenIsNull()` | `WHERE libre_token IS NULL` |
| **IsNotNull** | `findByLibreTokenIsNotNull()` | `WHERE libre_token IS NOT NULL` |
| **Between** | `findByCreatedAtBetween(Date start, Date end)` | `WHERE created_at BETWEEN ? AND ?` |
| **GreaterThan** | `findByChatIdGreaterThan(Long id)` | `WHERE chat_id > ?` |
| **GreaterThanEqual** | `findByChatIdGreaterThanEqual(Long id)` | `WHERE chat_id >= ?` |
| **LessThan** | `findByChatIdLessThan(Long id)` | `WHERE chat_id < ?` |
| **LessThanEqual** | `findByChatIdLessThanEqual(Long id)` | `WHERE chat_id <= ?` |
| **Not** | `findByChatIdNot(Long id)` | `WHERE chat_id <> ?` |
| **In** | `findByChatIdIn(List<Long> ids)` | `WHERE chat_id IN (?, ?, …)` |
| **NotIn** | `findByChatIdNotIn(List<Long> ids)` | `WHERE chat_id NOT IN (?, ?, …)` |
| **True / False** | `findByActiveTrue()` / `findByActiveFalse()` | `WHERE active = true` / `WHERE active = false` |
| **StartingWith** | `findByLibreMailStartingWith(String prefix)` | `WHERE libre_mail LIKE 'prefix%'` |
| **EndingWith** | `findByLibreMailEndingWith(String suffix)` | `WHERE libre_mail LIKE '%suffix'` |
| **Containing** | `findByLibreMailContaining(String text)` | `WHERE libre_mail LIKE '%text%'` |
| **Exists** | `existsByLibreMail(String mail)` | `SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM ... WHERE libre_mail = ?` |
| **Distinct** | `findDistinctByLibreMail(String mail)` | `SELECT DISTINCT * FROM ... WHERE libre_mail = ?` |
| **IgnoreCase** | `findByLibreMailIgnoreCase(String mail)` | `WHERE LOWER(libre_mail) = LOWER(?)` |
| **After** | `findByCreatedAtAfter(Date date)` | `WHERE created_at > ?` |
| **Before** | `findByCreatedAtBefore(Date date)` | `WHERE created_at < ?` |
| **Top / First** | `findTop1ByOrderByCreatedAtDesc()` | `ORDER BY created_at DESC LIMIT 1` |
| **CountBy** | `countByPatientId(Long id)` | `SELECT COUNT(*) FROM ... WHERE patient_id = ?` |
| **DeleteBy / RemoveBy** | `deleteByChatId(Long id)` | `DELETE FROM ... WHERE chat_id = ?` |
| **ExistsBy** | `existsByChatId(Long id)` | `SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END FROM ... WHERE chat_id = ?` |
| **NotLike** | `findByLibreMailNotLike(String pattern)` | `WHERE libre_mail NOT LIKE ?` |
| **Null / NotNull (альтернативные)** | `findByLibreTokenNull()` / `findByLibreTokenNotNull()` | `WHERE libre_token IS NULL` / `WHERE libre_token IS NOT NULL` |

> 🔹 **Примечание:**  
> В реальной практике чаще всего используются комбинации: `findBy`, `And`, `Or`, `OrderBy`, `Like`, `Between`, `IsNull`, `GreaterThan`, `LessThan`, а также `CountBy` и `ExistsBy` для проверок и подсчётов.
