An execution trace of `transferTo` for two threads:

|    | Thread S             |      |      | Shared |       | Thread T             |      |      |
|----|----------------------|------|------|--------|-------|----------------------|------|------|
|    | `a.transferTo(b,10)` |`tmp1`|`tmp2`|`a.bal` |`b.bal`| `b.transferTo(a,20)` |`tmp1`|`tmp2`|
| 1  |                      |      |      | 100    | 100   |                      |      |      |
| 2  | `tmp1 = a.bal-10`    | 90   |      | 100    | 100   |                      |      |      |
| 3  |                      | 90   |      | 100    | 100   | `tmp1 = b.bal-20`    | 80   |      |
| 4  |                      | 90   |      | 100    | 80    | `b.bal = tmp1`       | 80   |      |
| 5  |                      | 90   |      | 100    | 80    | `tmp2 = a.bal+20`    | 80   | 120  |
| 6  |                      | 90   |      | 120    | 80    | `a.bal = tmp2`       | 80   | 120  |
| 7  | `a.bal = tmp1`       | 90   |      | 90     | 80    |                      |      |      |
| 8  | `tmp2 = b.bal+10`    | 90   | 90   | 90     | 80    |                      |      |      |
| 9  | `b.bal = tmp2`       | 90   | 90   | 90     | 90    |                      |      |      |
| 10 |                      |      |      | 90     | 90    |                      |      |      |

Table 2 doesn't seem to be included, and the above table is already complete, not really sure what either the workbook
or the "t o d o" are getting at, but have copied table 2 in below anyway

|    | Thread S             |      |      | Shared |       | Thread T             |      |      |
|----|----------------------|------|------|--------|-------|----------------------|------|------|
|    | `a.transferTo(b,10)` |`tmp1`|`tmp2`|`a.bal` |`b.bal`| `b.transferTo(a,20)` |`tmp1`|`tmp2`|
| 1  |                      |      |      | 100    | 100   |                      |      |      |
| 2  | `tmp1 = a.bal-10`    | 90   |      | 100    | 100   |                      |      |      |
| 3  |                      | 90   |      | 100    | 100   | `tmp1 = b.bal-20`    | 80   |      |
| 4  |                      | 90   |      | 100    | 80    | `b.bal = tmp1`       | 80   |      |
| 5  | `a.bal = tmp1`       | 90   |      | 90     | 80    |                      |      |      |
| 6  | `tmp2 = b.bal+10`    |      | 90   | 90     | 80    |                      |      |      |
| 7  | `b.bal = tmp2`       |      | 90   | 90     | 90    |                      |      |      |
| 8  |                      |      |      | 90     | 90    | `tmp2 = a.bal+20`    |      | 110  |
| 9  |                      |      |      | 110    | 90    | `a.bal = tmp2`       |      | 110  |
| 10 |                      |      |      | 110    | 90    |                      |      |      |

The final result is correct