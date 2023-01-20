## Παραδοχές
- Κάθε client ξεκινά την επικοινωνία του (request) με τον server αιτούμενος μια συγκεκριμένη λειτουργία, λαμβάνει την απόκρισή του και τερματίζει τη σύνδεση. 
- Υπάρχει μοναδικός server που εξυπηρετεί (reply) αιτήματα, σερβίροντας την κατάλληλη απόκριση (απάντηση, μήνυμα σφάλματος ή επιβεβαίωσης) στον αιτούμενο client. Ο server δύναται να εξυπηρετήσει πολλαπλά αιτήματα (από διαφορετικούς client) ταυτόχρονα, όντας υλοποιημένος με την τεχνολογία *RMI*, που επιτρέπει (αυτόματα) multithreading.
- Ο server παραμένει σε λειτουργία μετά την εξυπηρέτηση κάθε αιτήματος client και διατηρεί πληροφορίες ιστορικού/καταλόγου καθόλη τη διάρκεια λειτουργίας του, σχετικά με τους λογαριασμούς χρηστών, το ιστορικό και την κατάσταση μηνυμάτων. Μετά τον τερματισμό (διακοπή λειτουργίας) του, τα δεδομένα χάνονται, καθώς αποθηκεύονται μόνο στην κύρια μνήμη του συστήματος.
- Για την ομαλή λειτουργία του συστήματος (όχι στην περίπτωση εκτέλεσης των *jar* αρχείων), απαιτείται πρώτα μεταγλώττιση

<br /> 

## Κλάσεις
Για την υλοποίηση του project, αναπτύχθηκαν οι εξής 5 κλάσεις και 1 διεπαφή (interface) σε γλώσσα Java.

<br /> 

### 1. `class Client`
Η κλάση `Client` αναπαριστά τον χρήστη του συστήματος που αποστέλει αιτήματα στο σύστημα (σ.σ. server) προς εξυπηρέτηση. Η δημιουργία request γίνεται με την εντολή:

    > java Client <server-ip> <server-port> <function-id> <arguments>

Η λειτουργία της κλάσης βασίζεται στο *parsing* των παραμέτρων εισόδου (`String[] args`) και την κλήση της κατάλληλης μεθόδου που είναι εκχωρημένη στην RMI registry του συστήματος του Server, δηλαδή εντοπίζει την RMI registry του δικτύου του server μέσω των `server-ip` και `server-port`, και καλεί τις μεθόδους που έχουν αντιστοιχηθεί από την εκφώνηση στο δηλωθέν `function-id`. Τα `arguments` συνιστούν μια ακολουθίας από μία τουλάχιστον συμβολοσειρά και περνούν από *parsing* βάσει του `function-id`.

Μόλις εκτελεστεί η συνάρτηση `main` της κλάσης με τα κατάλληλα ορίσματα, εντοπίζεται η RMI registry του server, υποβάλλεται το αίτημα για εκτέλεση της μεθόδου που έχει καταχωρηθεί και λαμβάνει απόκριση, που εμφανίζεται στο τερματικό.

<br />

### 2. `class Server`
Η κλάση `Server` αναπαριστά το σύστημα ελέγχου, διαχείρισης και υλοποίησης της επικοινωνίας μεταξύ των χρηστών (σ.σ. client(s)), απαντώντας/ικανοποιώντας τα αιτήματά τους ή σερβίροντας τα αιτούμενα δεδομένα. Η απάντηση (reply) γίνεται με την εντολή:

    > java Server <server-port>

Η λειτουργία της κλάσης βασίζεται στην *εγκαθίδρυση* μιας RMI registry σε συγκεκριμένο `port` της τρέχουσας `ip` διεύθυνσης του Server, στην οποία συνδέει/αντιστοιχεί τις υπογραφές των μεθόδων λειτουργίας (functions- βλ. `interface Commands`) με τις υλοποιήσεις τους για τη λειτουργία του, όπως ο server ορίζει (βλ. `class CommandExecution`). Αυτό επιτυγχάνεται μέσω της αντιστοίχισης του alias *commands* με το αντικείμενο *stub* που πλαισιώνει τις υλοποιήσεις των μεθόδων. Ο client, με το alias που ο server ορίζει και καταχωρεί στη registry του, γνωρίζει και καλεί τις μεθόδους που του υποδεικνύει το *stub*, το οποίο γνωρίζει ότι είναι τύπου `Commands`. Ουσιαστικά, λοιπόν, μέσω της registry, ο client "κουμπώνει" τις υλοποιήσεις που ο server υποδεικνύει για το interface που γνωρίζουν και οι δύο.

Μόλις εκτελεστεί η συνάρτηση `main` της κλάσης με τα κατάλληλα ορίσματα, δημιουργείται η RMI registry του server, καταχωρείται η υλοποίηση-κλάση `CommandExecution` για τη διεπαφασή `Commands` και ο server παραμένει σε ετοιμότητα για λήψη και εξυπηρέτηση αιτημάτων από έναν ή περισσότερους clients. Αξίζει να σημειωθεί πως μπορεί να εξυπηρετήσει ταυτόχρονα πολλούς clients, χάρη στους μηχανισμού πολυνηματισμού της τεχνολογίας RMI.

<br /> 

### 3. `class Account`
Η κλάση `Account` αναπαριστά έναν τυπικό χρήστη του συστήματος, ο οποίος περιγράφεται από έναν μοναδικό αναγνωριστικό κωδικό `authToken`, το όνομα `username` του, τη λίστα των μηνυμάτων του `messageBox` και έναν μετρητή μηνυμάτων `messageCounter`, ο οποίος λειτουργεί σαν αύξων αριθμός, χωρίς να ενημερώνεται σε περίπτωση διαφραφής.
Η κλάση περιλαμβάνει ακόμη μεθόδους τύπου *getter* για τα προαναφερθέντα, *private* χαρακτηριστικά της.

<br /> 

### 4. `class Message`
Η κλάση `Message` αναπαριστά ένα μήνυμα για το σύστημα, το οποίο περιγράφεται από μία λογική μεταβλητή αναγνωστικής κατάστασης `isRead`, τα ονόματα του αποστολέα `sender` και του παραλήπτη `receiver`, το σώμα του μηνύματος `body` και ένα αναγνωριστικό κωδικό `id` που αντικατοπτρίζει τον επόμενο `messageCounter` αριθμό του παραλήπτη. Αξίζει να σημειωθεί ότι οι κωδικοί των μηνυμάτων αφορέρονται στη λίστα μηνυμάτων κάθε χρήστη και, συνεπώς, προσδιορίζουν το μήνυμα σε συνδυασμό με το `Account` στου οποίου το `messageBox` ανήκουν.
Η κλάση περιλαμβάνει ακόμη μεθόδους τύπου *getter* και *setter* για τα προαναφερθέντα, *private* χαρακτηριστικά της.

<br />

### 5. `interface Commands`
Η διεπαφή `Commands` ορίζει τις έξι (6) λειτουργίες που μπορεί ένας χρήστης να αιτηθεί μέσω ενός client. Περιλαμβάνει τις αφηρημένες μεθόδους:

- `String createAccount(String username)
` για τη δημιουργία λογαριασμού στο σύστημα.
- `String showAccounts(int authToken)
` για την προβολή όλων των δημιουργήμενων λογαριασμών του συστήματος.
- `String sendMessage(int authToken, String receiver, String messageBody)
` για την αποστολή μηνύματος από έναν χρήστη προς έναν άλλο.
- `String showInbox(int authToken)
` για την προβολή του γραμματοκιβωτίου του χρήστη.
- `String readMessage(int authToken, int messageID)
` για την προβολή (διάβασμα) ενός παραληφθέντος μηνύματος από έναν χρήστη.
- ` String deleteMessage(int authToken, int messageID)
` για τη διαγραφή ενός παραληφθέντος μηνύματος από έναν χρήστη.

<br />

### 6. `class CommandExecution`
Η κλάση `CommandExecution` αναπαριστά την υλοποίηση των μεθόδων της διεπαφής `Commands`, όπως ο server επιλέγει να ορίσει. Διατηρεί και διαχειρίζεται, ακόμη, τη λίστα όλων των δημιουργημένων λογαριασμών χρήστη `List<Account> accountList` και έναν αύξοντα αριθμό `tokenCounter` (ο οποίος ξεκινά από την τιμή **1001** για τον **1ο** χρήστη) για την απόδοση `authToken` σε κάθε νέο χρήστη (`Account`). 

Περιλαμβάνει μεθόδους τύπου *getter* και *setter* για τα *private* χαρακτηριστικά της κλάσης και τις εξής βοηθητικές, *private* μεθόδους:
- `static boolean isValidUsername(String name)` που ελέγχει αν το username που επιλέγει ένας χρήστης για τον λογαριασμό που δημιουργεί είναι έγκυρο: ξεκινάει από αλφαβητικό χαρακτήρα και περιλαμβάνει αλφαριθμητικούς χαρακτήρες μόνο.
- `Account getUserByToken(int authToken)` που επιστρέφει αντικείμενο τύπου `Account` για δοθέν αναγνωριστικό αριθμό `authToken`, εάν αυτό αντιστοιχεί σε υπάρχοντα λογαριασμό.
- `Account getUserByUsername(String username)` που επιστρέφει αντικείμενο τύπου `Account` για δοθέν (αλφαριθμητικό) username, εάν αυτό αντιστοιχεί σε υπάρχοντα λογαριασμό.
- `Message getMessageById(Account a, int id)` που επιστρέφει αντικείμενο τύπου `Message` από τη λίστα των μηνυμάτων του χρήστη `a` με κωδικό μηνύματος `id`, εφόσον αυτό υπάρχει.

Πέραν των βοηθητικών αυτών μεθόδων, έχουν υλοποιηθεί (`@Override`) οι έξι (6) λειτουργίες (μέθοδοι) που περιγράφονται στην εκφώνηση και οι περισσότερες απαιτούν και βασίζονται στον **έλεγχο επικύρωσης του `authToken`** του χρήστη που αιτείται ενέργεια, πριν την εκτέλεση της εκάστοτε δραστηριότητας και την αποστολή *reply*.
> Λεπτομέρειες υλοποίησης μπορούν να βρεθούν στα σχόλια που συνοδεύουν τον κώδικα.

Συνεπώς, με την εν λόγω κλάση ο server έχει καθορίσει την ανάπτυξη των υπογραφών των μεθόδων-λειτουργιών που οι clients γνωρίζουν και καλούν για την υποβολή των αιτημάτων τους (*requests*).