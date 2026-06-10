package com.example.data

object MockStaticData {

    // Simple relative visual coordinate helpers for nodes in mind maps
    val topics = listOf(
        GkGsTopic(
            id = "history_indus",
            name = "Indus Valley Civilization",
            subject = "History",
            videoId = "history_v1",
            videoTitle = "Harappan Civilization: Urban Planning, Trade, & Tech",
            duration = "14:20",
            mindMapUrl = "history_indus_map",
            revisionNotes = """
                • Indus Valley Civilization (IVV) was an Ancient Bronze Age civilization (2500 BCE – 1750 BCE).
                • Key Sites: 
                  - Mohenjo-daro: Great Bath, Bronze Dancing Girl, Assembly Hall.
                  - Harappa: Granaries, Coffin burials.
                  - Lothal: Ancient dockyard, bead assembly unit, fire altars.
                  - Kalibangan: Ploughed field, fire altars.
                  - Dholavira: Tripartite fortification, unique water reservoir system.
                • Drainage System: Unique underground grid drainage with manholes.
                • Trade: Sealed tablets, copper beads, trade relations with Mesopotamia (Sumerians).
                • Script: Pictographic, undeciphered (Boustrophedon style - alternating directions).
            """.trimIndent(),
            mindMapNodes = listOf(
                MindMapNode("h1", "Indus Valley (2500-1700 BCE)", 0.5f, 0.15f, listOf("h2", "h3", "h4", "h5"), "The earliest urban civilization in Indian subcontinent."),
                MindMapNode("h2", "Major Sites", 0.2f, 0.45f, listOf("h2_1", "h2_2"), "Cities discovered along Indus and Saraswati rivers."),
                MindMapNode("h2_1", "Mohenjo-daro", 0.1f, 0.75f, emptyList(), "The 'Mound of Dead'. Home to the Great Bath."),
                MindMapNode("h2_2", "Lothal", 0.3f, 0.75f, emptyList(), "Prominent shipyard on the Gujarat coast."),
                MindMapNode("h3", "Urban Planning", 0.5f, 0.55f, emptyList(), "Built on standard grid system. Burnt brick dwellings with elaborate covered drainage system."),
                MindMapNode("h4", "Economy & Trade", 0.8f, 0.45f, emptyList(), "Agriculture-based + trading system using standard seals. Traded with Mesopotamia."),
                MindMapNode("h5", "Decline", 0.8f, 0.75f, emptyList(), "Gradual ecological shifts, floods, or drying up of rivers like Saraswati.")
            )
        ),
        GkGsTopic(
            id = "history_freedom",
            name = "Indian Freedom Struggle (1857-1947)",
            subject = "History",
            videoId = "history_v2",
            videoTitle = "The rise of Nationalism and Gandhian Phases",
            duration = "18:45",
            mindMapUrl = "history_freedom_map",
            revisionNotes = """
                • Revolt of 1857: Indian Mutiny, sparked at Meerut by Mangal Pandey. Under Bahadur Shah Zafar.
                • Establishment of INC: Founded in 1885 by A.O. Hume. First session led by W.C. Bonnerjee in Bombay.
                • Partition of Bengal: 1905 by Lord Curzon. Sparked the Swadeshi Movement.
                • Gandhian Era (1915-1947):
                  - Champaran Satyagraha (1917) - First active Satyagraha against Indigo farmers.
                  - Non-Cooperation Movement (1920-1922) - Suspended after Chauri Chaura incident.
                  - Civil Disobedience & Salt March (1930) - Dandi March against salt taxes.
                  - Quit India Movement (1942) - "Do or Die" call by Gandhiji.
                • Indian Independence Act (1947): Lord Mountbatten Plan partitioned British India into India & Pakistan.
            """.trimIndent(),
            mindMapNodes = listOf(
                MindMapNode("f1", "Freedom Struggle", 0.5f, 0.15f, listOf("f2", "f3", "f4"), "The trajectory of Indian resistance against British Raj."),
                MindMapNode("f2", "1857 Revolt", 0.2f, 0.45f, emptyList(), "First War of Independence, Mangal Pandey, Bahadur Shah II."),
                MindMapNode("f3", "INC Formation (1885)", 0.5f, 0.5f, listOf("f3_1", "f3_2"), "Formed by A.O. Hume. Moderate and Extremist divisions."),
                MindMapNode("f3_1", "Moderates", 0.4f, 0.8f, emptyList(), "Dadabhai Naoroji, Pherozeshah Mehta, G.K. Gokhale. Peaceful advocacy."),
                MindMapNode("f3_2", "Extremists", 0.6f, 0.8f, emptyList(), "Lal-Bal-Pal (Lajpat Rai, Tilak, B.C. Pal). Radical actions."),
                MindMapNode("f4", "Gandhian Stage (1915+)", 0.8f, 0.45f, emptyList(), "Non-Cooperation (1920), Civil Disobedience (1930), Quit India (1942).")
            )
        ),
        GkGsTopic(
            id = "polity_const",
            name = "Indian Constitution & Preamble",
            subject = "Polity",
            videoId = "polity_v1",
            videoTitle = "Making of the Indian Constitution & Features",
            duration = "16:10",
            mindMapUrl = "polity_const_map",
            revisionNotes = """
                • Constituent Assembly: Set up in 1946 under Cabinet Mission Plan. First meeting Dec 9, 1946.
                • Drafting Committee Chairman: Dr. B.R. Ambedkar (called Father of Indian Constitution).
                • Adoption Dates: Adopted on 26th Nov 1949. Came into effect on 26th Jan 1950 (celebrated as Republic Day).
                • Sovereign, Socialist, Secular, Democratic, Republic added/specified in Preamble. 
                  - Words "Socialist", "Secular", and "Integrity" added by the 42nd Amendment (1976).
                • Borrowed Features:
                  - Fundamental Rights: USA
                  - Directive Principles (DPSP): Ireland
                  - Fundamental Duties: USSR / Russia
                  - Parliamentary System: United Kingdom
            """.trimIndent(),
            mindMapNodes = listOf(
                MindMapNode("p1", "Indian Constitution", 0.5f, 0.15f, listOf("p2", "p3", "p4"), "The supreme legal framework governing India."),
                MindMapNode("p2", "Historical Making", 0.2f, 0.45f, emptyList(), "Drafting Committee headed by B.R. Ambedkar. Took 2 years, 11 months, 18 days."),
                MindMapNode("p3", "Preamble Matrix", 0.5f, 0.55f, listOf("p3_1", "p3_2"), "The soul of the constitution. Sovereign, Socialist, Secular, Democratic, Republic."),
                MindMapNode("p3_1", "42nd Amendment", 0.4f, 0.85f, emptyList(), "Significant amendment in 1976. Added Socialist, Secular, Integrity."),
                MindMapNode("p3_2", "Pillars", 0.6f, 0.85f, emptyList(), "Justice, Liberty, Equality, and Fraternity for all citizens."),
                MindMapNode("p4", "Sourced Features", 0.8f, 0.45f, emptyList(), "Borrowed from USA (Rights), UK (Parliament), Ireland (DPSP), USSR (Duties).")
            )
        ),
        GkGsTopic(
            id = "polity_rights",
            name = "Fundamental Rights & Duties",
            subject = "Polity",
            videoId = "polity_v2",
            videoTitle = "Article 12 to 35: Right to Equality, Freedom & Writs",
            duration = "15:30",
            mindMapUrl = "polity_rights_map",
            revisionNotes = """
                • Part III of Constitution (Articles 12 to 35) represents Fundamental Rights. Sourced from US Bill of Rights.
                • Total Categories (6):
                  1. Right to Equality (Articles 14-18)
                  2. Right to Freedom (Articles 19-22)
                  3. Right against Exploitation (Articles 23-24)
                  4. Right to Freedom of Religion (Articles 25-28)
                  5. Cultural and Educational Rights (Articles 29-30)
                  6. Right to Constitutional Remedies (Article 32)
                • Article 32: Heart and Soul according to B.R. Ambedkar. Empowers Supreme Court to issue Writs: Habeas Corpus, Mandamus, Prohibition, Certiorari, and Quo Warranto.
                • Article 21A: Right to Education, added by 86th Amendment Act (2002).
                • Fundamental Duties: Found in Part IV-A, Article 51A. 10 added by 42nd Amendment (1976); 11th added by 86th Amendment. Sourced from USSR.
            """.trimIndent(),
            mindMapNodes = listOf(
                MindMapNode("fr1", "Rights and Duties", 0.5f, 0.15f, listOf("fr2", "fr3", "fr4"), "Core rights of Indian citizens and duties towards the nation."),
                MindMapNode("fr2", "Part III: Rights", 0.2f, 0.45f, listOf("fr2_1", "fr2_2"), "Justiciable rights protected by the Supreme Court directly."),
                MindMapNode("fr2_1", "Article 21", 0.1f, 0.75f, emptyList(), "Right to Life & Personal Liberty. Broad scope (Privacy, Dignity)."),
                MindMapNode("fr2_2", "Article 32", 0.3f, 0.75f, emptyList(), "Constitutional Remedies. Allows issuing Writs of Habeas Corpus, Mandamus etc."),
                MindMapNode("fr3", "Part IV-A: Duties", 0.8f, 0.45f, listOf("fr3_1"), "Non-justiciable duties of citizenship, added in 1976."),
                MindMapNode("fr3_1", "51A Duties", 0.8f, 0.75f, emptyList(), "Started with 10 duties. 11th added via 86th Amendment in 2002 for Child Education.")
            )
        ),
        GkGsTopic(
            id = "geo_rivers",
            name = "Indian River Systems",
            subject = "Geography",
            videoId = "geo_v1",
            videoTitle = "Himalayan vs Peninsular River Basins",
            duration = "17:15",
            mindMapUrl = "geo_rivers_map",
            revisionNotes = """
                • Classified into two major categories: Himalayan Rivers and Peninsular Rivers.
                • Himalayan Rivers: Perennial rivers (flow year-round). Fed by rain and melting glaciers.
                  - Indus System: Originates in Tibet near Mansarovar. Tributaries: Jhelum, Chenab, Ravi, Beas, Sutlej.
                  - Ganga System: Formed by Alaknanda and Bhagirathi joining at Devprayag. Left tributaries include Yamuna (longest tributary), Gomti, Ghaghara, Gandak, Kosi.
                  - Brahmaputra System: Known as Tsangpo in Tibet and Dihang in Arunachal Pradesh. Forms world's largest delta (Sundarbans).
                • Peninsular Rivers: Seasonal rivers. Main rainwater-fed streams.
                  - West Flowing (into Arabian Sea): Narmada (flows in Rift Valley), Tapi, Sabarmati.
                  - East Flowing (into Bay of Bengal): Mahanadi, Godavari (called Dakshin Ganga - longest Peninsular river), Krishna, Kaveri.
            """.trimIndent(),
            mindMapNodes = listOf(
                MindMapNode("g1", "Indian Rivers", 0.5f, 0.15f, listOf("g2", "g3"), "The arterial river basins of India."),
                MindMapNode("g2", "Himalayan Rivers", 0.3f, 0.45f, listOf("g2_1", "g2_2"), "Perennial, high sedimentation, large basins."),
                MindMapNode("g2_1", "Ganga Basin", 0.15f, 0.75f, emptyList(), "Largest basin in India, forms Sunderbans Delta with Brahmaputra."),
                MindMapNode("g2_2", "Indus System", 0.4f, 0.75f, emptyList(), "Originates near Lake Mansarovar. Followed by Treaty of 1960."),
                MindMapNode("g3", "Peninsular Rivers", 0.7f, 0.45f, listOf("g3_1", "g3_2"), "Rain-fed, seasonal, older river courses."),
                MindMapNode("g3_1", "Godavari", 0.65f, 0.75f, emptyList(), "The 'Dakshin Ganga'. Longest Peninsular river."),
                MindMapNode("g3_2", "Narmada & Tapi", 0.85f, 0.75f, emptyList(), "Westward-flowing rift valley rivers draining into the Arabian Sea.")
            )
        ),
        GkGsTopic(
            id = "science_cells",
            name = "Cell Biology & Human Body Systems",
            subject = "Science",
            videoId = "science_v1",
            videoTitle = "Cell Structure, Functions, and Major Human Organs",
            duration = "13:25",
            mindMapUrl = "science_cells_map",
            revisionNotes = """
                • Cell: Structural and functional unit of life. Discovered by Robert Hooke (1665).
                • Major Organelles:
                  - Nucleus: Brain of the cell, contains genetics (DNA, chromosomes).
                  - Mitochondria: Powerhouse of the cell, generates ATP.
                  - Ribosome: Site of protein synthesis.
                  - Lysosome: Suicidal bags, contains digestive enzymes.
                  - Chloroplast: Site of plant photosynthesis (only in plant cells).
                • Human Organs / Systems:
                  - Circulatory: Heart (4 Chambers), Blood (Plasma, Red Cells - live 120 days, White Cells, Platelets).
                  - Nervous System: Central nervous system (Brain, Spinal cord). Unit is Neuron.
                  - Digestive System: Starts in mouth, main nutrient absorption occurs in Small Intestine.
                  - Excretory System: Kidneys. Structural unit is Nephron.
            """.trimIndent(),
            mindMapNodes = listOf(
                MindMapNode("s1", "Biology & Anatomy", 0.5f, 0.15f, listOf("s2", "s3"), "Fundamental building blocks of living organisms & human bodies."),
                MindMapNode("s2", "Cell Structure", 0.3f, 0.45f, listOf("s2_1", "s2_2"), "Plant and animal cells containing organelles."),
                MindMapNode("s2_1", "Mitochondria", 0.15f, 0.75f, emptyList(), "Generates cellular energy (ATP). Powerhouse."),
                MindMapNode("s2_2", "Lysosomes", 0.4f, 0.75f, emptyList(), "Suicidal bags clearing cellular waste."),
                MindMapNode("s3", "Organ Systems", 0.7f, 0.45f, listOf("s3_1", "s3_2"), "Integrated systems coordinating bodily life functions."),
                MindMapNode("s3_1", "Kidney/Nephron", 0.6f, 0.75f, emptyList(), "Filters blood, manages water-salt balance."),
                MindMapNode("s3_2", "Heart & Blood", 0.85f, 0.75f, emptyList(), "Pumps blood. RBC lifespan is ~120 days. WBC handles immunity.")
            )
        ),
        GkGsTopic(
            id = "banking_rbi",
            name = "RBI and Monetary Policy Metrics",
            subject = "Economy & Banking",
            videoId = "banking_v1",
            videoTitle = "Reserve Bank of India: Repo Rate, Reverse Repo & Inflation Control",
            duration = "15:55",
            mindMapUrl = "banking_rbi_map",
            revisionNotes = """
                • RBI: Reserve Bank of India, established April 1, 1935 under RBI Act 1934 on Hilton Young Commission recommendation. Nationalized on January 1, 1949.
                • Monetary Policy Committee (MPC): 6-member panel that meets bimonthly. Headed by the RBI Governor.
                • Qualitative & Quantitative Tools:
                  - Repo Rate: Rate at which RBI lends money to commercial banks (security backed). High repo rate curbs inflation.
                  - Reverse Repo Rate: Interest commercial banks receive for parking liquid funds with RBI.
                  - Cash Reserve Ratio (CRR): Percentage of net bank deposits banks must reserve under liquid cash with RBI.
                  - Statutory Liquidity Ratio (SLR): Percentage of deposits banks must keep in liquid assets like gold, govt securities before lending.
                  - Marginal Standing Facility (MSF) & Bank Rate: Overnight reserve credit rates.
                • Fiscal Policy: Handled by central Government Finance Ministry (not RBI).
            """.trimIndent(),
            mindMapNodes = listOf(
                MindMapNode("b1", "Reserve Bank of India", 0.5f, 0.15f, listOf("b2", "b3", "b4"), "Central bank regulating fiscal confidence and credit stability."),
                MindMapNode("b2", "Establishment (1935)", 0.2f, 0.45f, emptyList(), "Hilton-Young Commission. Nationalized in 1949. Issued first currency note in 1938."),
                MindMapNode("b3", "Monetary Policy", 0.5f, 0.55f, listOf("b3_1", "b3_2"), "Formulated by MPC to manage price levels & economic growth."),
                MindMapNode("b3_1", "Repo Rate", 0.35f, 0.85f, emptyList(), "Lending rate to commercial banks. Raising Repo lowers market inflation."),
                MindMapNode("b3_2", "CRR & SLR", 0.65f, 0.85f, emptyList(), "Reserves required as cash with RBI (CRR) or sovereign paper/gold in-vault (SLR)."),
                MindMapNode("b4", "Functions", 0.8f, 0.45f, emptyList(), "Currency issuing, banker's bank, and custodian of foreign exchange reserves.")
            )
        ),
        GkGsTopic(
            id = "static_parks",
            name = "National Parks & Wildlife in India",
            subject = "Static GK",
            videoId = "static_v1",
            videoTitle = "Indian National Parks & UNESCO Wilderness Hotspots",
            duration = "12:50",
            mindMapUrl = "static_parks_map",
            revisionNotes = """
                • India has over 100 National Parks covering major geographical biomes.
                • Jim Corbett National Park (Uttarakhand): Established in 1936 as Hailey National Park, it is India's first National Park. Prominent home of Bengal Tiger.
                • Kaziranga National Park (Assam): Famous for hosting two-thirds of the world's great one-horned rhinoceroses. UNESCO World Heritage Site.
                • Gir National Park (Gujarat): The sole remaining natural habitat of the critically endangered Asiatic Lion.
                • Keoladeo National Park (Bharatpur, Rajasthan): World famous bird sanctuary, famous for migratory Siberian Cranes.
                • Hemis National Park (Ladakh): Highest altitude national park in India, famous for holding the highest density of snow leopards.
            """.trimIndent(),
            mindMapNodes = listOf(
                MindMapNode("st1", "National Parks of India", 0.5f, 0.15f, listOf("st2", "st3", "st4"), "Crucial ecological reserves and bio-conservation zones."),
                MindMapNode("st2", "Jim Corbett (1936)", 0.2f, 0.45f, emptyList(), "First national park, located in Uttarakhand. Pioneer under Project Tiger."),
                MindMapNode("st3", "Kaziranga (Assam)", 0.5f, 0.55f, emptyList(), "UNESCO World Heritage site home of the iconic One-Horned Rhinoceros."),
                MindMapNode("st4", "Keoladeo Bird Sanctuary", 0.8f, 0.45f, emptyList(), "Rajasthan. Former royal hunting reserve, outstanding hub for Siberian Cranes.")
            )
        ),
        GkGsTopic(
            id = "static_dances",
            name = "Classical & Folk Dances of India",
            subject = "Static GK",
            videoId = "static_v2",
            videoTitle = "Classical Dances of India: Sangeet Natak Akademi",
            duration = "11:40",
            mindMapUrl = "static_dances_map",
            revisionNotes = """
                • Sangeet Natak Akademi recognizes 8 principal classical dances of India (Ministry of Culture includes Chhau as 9th).
                • Key Classical Dances:
                  - Bharatnatyam (Tamil Nadu): Oldest classical dance, originating from temple dancers (Devadasis). Described in Natyashastra.
                  - Kathak (Uttar Pradesh): Storyteller format. Famous exponents: Birju Maharaj, Lachhu Maharaj.
                  - Kathakali (Kerala): Elaborate green face-mask makeup and epic action storyline.
                  - Kuchipudi (Andhra Pradesh): Uses brass plates, incorporates spoken words.
                  - Mohiniyattam (Kerala): Soft, feminine solos.
                  - Sattriya (Assam): Devotional Vaishnavite origin.
            """.trimIndent(),
            mindMapNodes = listOf(
                MindMapNode("da1", "Indian Classical Dances", 0.5f, 0.15f, listOf("da2", "da3", "da4"), "The soul of traditional dramatic art and worship."),
                MindMapNode("da2", "Bharatnatyam", 0.2f, 0.45f, emptyList(), "Tamil Nadu. Hand gestures (mudras) originating from temple devadasis."),
                MindMapNode("da3", "Kathak (UP)", 0.5f, 0.55f, emptyList(), "North Indian storytelling, complex footwork (tatkar) loops."),
                MindMapNode("da4", "Kathakali (Kerala)", 0.8f, 0.45f, emptyList(), "Dramatized storytelling with highly elaborate face makeup.")
            )
        )
    )

    // Preset questions matching the topic IDs. Includes realistic SSC/Railway/NDA questions!
    val questions = listOf(
        // History: Indus Valley
        QuizQuestion(
            id = "history_indus_q1",
            topicName = "Indus Valley Civilization",
            questionText = "Which among the following Harappan sites is known for possessing the ancient dockyard?",
            optionA = "Kalibangan",
            optionB = "Harappa",
            optionC = "Lothal",
            optionD = "Dholavira",
            correctAnswer = "C",
            explanation = "Lothal located on Bhogava river in Gujarat is known for an artificial tile-made brick dockyard used for regional and maritime trade."
        ),
        QuizQuestion(
            id = "history_indus_q2",
            topicName = "Indus Valley Civilization",
            questionText = "The famous 'Great Bath' of the Indus Valley Civilization was discovered at which site?",
            optionA = "Harappa",
            optionB = "Mohenjo-daro",
            optionC = "Chanhudaro",
            optionD = "Lothal",
            correctAnswer = "B",
            explanation = "Mohenjo-daro houses the famous 'Great Bath' - a large rectangular public pool constructed from baked bricks and sealed with bitumen."
        ),
        QuizQuestion(
            id = "history_indus_q3",
            topicName = "Indus Valley Civilization",
            questionText = "What was unique about the houses built in the Indus Valley Civilization?",
            optionA = "They had sloping roofs made of slate",
            optionB = "They were built of standard sized burnt bricks arranged with a grid layout",
            optionC = "They were circular huts made of wood and bamboo",
            optionD = "They had multi-level basement cells for cold storage",
            correctAnswer = "B",
            explanation = "Indus valley architecture utilized kiln-burnt bricks of consistent 1:2:4 ratio laid in grid structures with proper sewage connections."
        ),
        QuizQuestion(
            id = "history_indus_q4",
            topicName = "Indus Valley Civilization",
            questionText = "The Harappan civilization belonged principally to which historical age?",
            optionA = "Iron Age",
            optionB = "Neolithic Age",
            optionC = "Bronze Age",
            optionD = "Paleolithic Age",
            correctAnswer = "C",
            explanation = "Harappan culture was a highly sophisticated Bronze Age civilization that manufactured copper alloys despite not knowing iron."
        ),

        // History: Freedom Struggle
        QuizQuestion(
            id = "history_freedom_q1",
            topicName = "Indian Freedom Struggle (1857-1947)",
            questionText = "Who was the first president of the Indian National Congress (INC) during its initial meeting in 1885?",
            optionA = "Allan Octavian Hume",
            optionB = "Womesh Chandra Bonnerjee",
            optionC = "Dadabhai Naoroji",
            optionD = "Surendranath Banerjee",
            correctAnswer = "B",
            explanation = "The first session of INC was held in Bombay’s Gokuldas Tejpal Sanskrit College in December 1885, presided over by W.C. Bonnerjee and attended by 72 delegates."
        ),
        QuizQuestion(
            id = "history_freedom_q2",
            topicName = "Indian Freedom Struggle (1857-1947)",
            questionText = "Which Satyagraha marked Mahatma Gandhi’s first passive resistance movement in India?",
            optionA = "Kheda Satyagraha",
            optionB = "Ahmedabad Mill Strike",
            optionC = "Champaran Satyagraha",
            optionD = "Rowlatt Satyagraha",
            correctAnswer = "C",
            explanation = "In 1917, Champaran Satyagraha in Bihar was Gandhi's first civil disobedience action to fight against the exploitation of indigo planters."
        ),
        QuizQuestion(
            id = "history_freedom_q3",
            topicName = "Indian Freedom Struggle (1857-1947)",
            questionText = "In which historic session of Indian National Congress was the resolution of 'Poorna Swaraj' (Complete Independence) passed?",
            optionA = "Karachi Session (1931)",
            optionB = "Lahore Session (1929)",
            optionC = "Belgaum Session (1924)",
            optionD = "Calcutta Session (1920)",
            correctAnswer = "B",
            explanation = "Presided by Jawaharlal Nehru at Lahore, the INC Congress passed the landmark resolution demanding complete independence on December 31, 1929."
        ),

        // Polity: Constitution
        QuizQuestion(
            id = "polity_const_q1",
            topicName = "Indian Constitution & Preamble",
            questionText = "Which of the following amendments added the words 'Socialist', 'Secular' and 'Integrity' into the Preamble of India?",
            optionA = "44th Amendment Act",
            optionB = "42nd Amendment Act",
            optionC = "86th Amendment Act",
            optionD = "24th Amendment Act",
            correctAnswer = "B",
            explanation = "The 42nd Constitutional Amendment Act of 1976 modified the Preamble by adding the concepts of Socialism, Secularism, and Integrity."
        ),
        QuizQuestion(
            id = "polity_const_q2",
            topicName = "Indian Constitution & Preamble",
            questionText = "From which nation's constitution did the framers of India borrow the Directive Principles of State Policy (DPSP)?",
            optionA = "Ireland",
            optionB = "USA",
            optionC = "Soviet Union",
            optionD = "Australia",
            correctAnswer = "A",
            explanation = "The Directive Principles of State Policy (Part IV of the Indian Constitution) are inspired, with slight changes, by the Irish Constitution."
        ),
        QuizQuestion(
            id = "polity_const_q3",
            topicName = "Indian Constitution & Preamble",
            questionText = "On which historical day was the Constitution of India formally adopted by the Constituent Assembly?",
            optionA = "January 26, 1950",
            optionB = "November 26, 1949",
            optionC = "August 15, 1947",
            optionD = "December 9, 1946",
            correctAnswer = "B",
            explanation = "The Constitution was formally adopted on 26th November 1949 (now celebrated as Constitution Day) and eventually became law on 26th January 1950."
        ),

        // Polity: Rights
        QuizQuestion(
            id = "polity_rights_q1",
            topicName = "Fundamental Rights & Duties",
            questionText = "Which Article of the Indian Constitution is termed as the 'Heart and Soul' by Dr. B.R. Ambedkar?",
            optionA = "Article 19 (Right to Freedom)",
            optionB = "Article 21 (Right to Life)",
            optionC = "Article 32 (Right to Constitutional Remedies)",
            optionD = "Article 14 (Right to Equality)",
            correctAnswer = "C",
            explanation = "Article 32 guarantees individuals the fundamental right to approach the Supreme Court directly to seek justice when core rights are violated."
        ),
        QuizQuestion(
            id = "polity_rights_q2",
            topicName = "Fundamental Rights & Duties",
            questionText = "Which constitutional amendment introduced the Right to Free & Compulsory Education (Article 21A)?",
            optionA = "42nd Amendment",
            optionB = "44th Amendment",
            optionC = "86th Amendment",
            optionD = "91st Amendment",
            correctAnswer = "C",
            explanation = "Passed in 2002, the 86th Constitutional Amendment introduced Article 21A, making elementary education a fundamental right for children aged 6 to 14."
        ),

        // Geography: Rivers
        QuizQuestion(
            id = "geo_rivers_q1",
            topicName = "Indian River Systems",
            questionText = "Which peninsular river of India is frequently called 'Dakshin Ganga' owing to its vast sub-basin size?",
            optionA = "Krishna River",
            optionB = "Kaveri River",
            optionC = "Godavari River",
            optionD = "Narmada River",
            correctAnswer = "C",
            explanation = "Godavari is termed the longest river in the peninsula and is extensively known as 'Dakshin Ganga' (Ganga of the South)."
        ),
        QuizQuestion(
            id = "geo_rivers_q2",
            topicName = "Indian River Systems",
            questionText = "Which river flows in a distinctive rift valley westward between the Vindhya and Satpura mountain ranges?",
            optionA = "Kaveri",
            optionB = "Sabarmati",
            optionC = "Narmada",
            optionD = "Mahanadi",
            correctAnswer = "C",
            explanation = "Narmada River flows westward on a default geological rift valley, finally draining into the Gulf of Khambhat on the Arabian Sea."
        ),

        // Science: Cells
        QuizQuestion(
            id = "science_cells_q1",
            topicName = "Cell Biology & Human Body Systems",
            questionText = "Which cell organelle is frequently referred to as the 'Suicidal Bags' of the cell due to digestive enzyme actions?",
            optionA = "Ribosomes",
            optionB = "Lysosomes",
            optionC = "Mitochondria",
            optionD = "Golgi Apparatus",
            correctAnswer = "B",
            explanation = "Lysosomes store hydrolytic enzymes. If a cell is heavily damaged, they burst open and break down the cell's own structure, giving them this name."
        ),
        QuizQuestion(
            id = "science_cells_q2",
            topicName = "Cell Biology & Human Body Systems",
            questionText = "What is the average life span of human Red Blood Cells (RBCs) circulating in the bloodstream?",
            optionA = "20 to 30 Days",
            optionB = "120 Days",
            optionC = "1 Year",
            optionD = "10 to 12 Days",
            correctAnswer = "B",
            explanation = "Red blood cells (erythrocytes) have an average lifespan of 120 days in active blood circulation before being filtered and destroyed by the spleen."
        ),

        // Banking: RBI
        QuizQuestion(
            id = "banking_rbi_q1",
            topicName = "RBI and Monetary Policy Metrics",
            questionText = "What is the rate at which the Reserve Bank of India lends overnight funds to commercial banks against pledged securities?",
            optionA = "Reverse Repo Rate",
            optionB = "Statutory Liquidity Rate",
            optionC = "Repo Rate",
            optionD = "Bank Rate",
            correctAnswer = "C",
            explanation = "Repo Rate is the policy repo rate wherein banks borrow from RBI under repurchase transactions to cover short term assets."
        ),
        QuizQuestion(
            id = "banking_rbi_q2",
            topicName = "RBI and Monetary Policy Metrics",
            questionText = "In which year was the Reserve Bank of India nationalized under the sovereign ownership of the Government of India?",
            optionA = "1935",
            optionB = "1947",
            optionC = "1949",
            optionD = "1951",
            correctAnswer = "C",
            explanation = "While established as a private shareholder bank in 1935, RBI was officially nationalized soon after independence on January 1, 1949."
        ),
        QuizQuestion(
            id = "static_parks_q1",
            topicName = "National Parks & Wildlife in India",
            questionText = "Which among the following enjoys the prestige of being India's very first established National Park?",
            optionA = "Jim Corbett National Park",
            optionB = "Kaziranga National Park",
            optionC = "Gir Forest National Park",
            optionD = "Hemis High Altitude National Park",
            correctAnswer = "A",
            explanation = "Jim Corbett National Park in Uttarakhand was established in 1936 under the name 'Hailey National Park'. It was later renamed to honor naturalist Jim Corbett."
        ),
        QuizQuestion(
            id = "static_parks_q2",
            topicName = "National Parks & Wildlife in India",
            questionText = "The Kaziranga National Park, renowned worldwide for hosting the majestic one-horned rhinoceroses, is situated in which state?",
            optionA = "West Bengal",
            optionB = "Assam",
            optionC = "Arunachal Pradesh",
            optionD = "Meghalaya",
            correctAnswer = "B",
            explanation = "Kaziranga is located in Golaghat and Nagaon districts of Assam, and is an acclaimed UNESCO World Heritage site with high tiger density and unique rhino populations."
        ),
        QuizQuestion(
            id = "static_dances_q1",
            topicName = "Classical & Folk Dances of India",
            questionText = "Which of the following classical dance forms originated and remains deeply rooted in the temple devadasis tradition of Tamil Nadu?",
            optionA = "Kathakali",
            optionB = "Bharatnatyam",
            optionC = "Kuchipudi",
            optionD = "Sattriya",
            correctAnswer = "B",
            explanation = "Bharatnatyam is the oldest classical dance which originated as 'Sadir Attam' performed in temples of southern Tamil Nadu before obtaining national recognition."
        ),
        QuizQuestion(
            id = "static_dances_q2",
            topicName = "Classical & Folk Dances of India",
            questionText = "Which classical dance of Kerala is globally identified by elaborate face-mask makeups and vivid theatrical acting of ancient epics?",
            optionA = "Kuchipudi",
            optionB = "Kathak",
            optionC = "Kathakali",
            optionD = "Mohiniyattam",
            correctAnswer = "C",
            explanation = "Kathakali is highly popular for its massive colorful masks, green paint representational status, and complex dramatic sign language performing epics like Ramayana."
        )
    )
}
