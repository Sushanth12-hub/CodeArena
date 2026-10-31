# Manual test checklist

| Area | Check |
|---|---|
| Registration | A valid new student can register and duplicate email is rejected. |
| Login | Invalid credentials are rejected; admin and student receive the correct dashboard. |
| Access control | A student cannot open `/admin`. |
| Learning | A subject shows its topics and every topic shows a note. |
| Quiz | Five questions render; blank answers score zero; result is calculated correctly. |
| History | A submitted attempt appears in the signed-in student's history. |
| Admin | Admin can add a question and view all saved attempts. |
| Database | Invalid foreign-key relationships are rejected by the schema. |
