package com.movie.ticket.config;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.movie.ticket.entity.Movie;
import com.movie.ticket.entity.Show;
import com.movie.ticket.entity.Theatre;
import com.movie.ticket.entity.User;
import com.movie.ticket.repository.MovieRepository;
import com.movie.ticket.repository.ShowRepository;
import com.movie.ticket.repository.TheatreRepository;
import com.movie.ticket.repository.UserRepository;

import org.springframework.jdbc.core.JdbcTemplate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final TheatreRepository theatreRepository;
    private final ShowRepository showRepository;
    private final JdbcTemplate jdbcTemplate;

    public DataInitializer(UserRepository userRepository,
                           MovieRepository movieRepository,
                           TheatreRepository theatreRepository,
                           ShowRepository showRepository,
                           JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.theatreRepository = theatreRepository;
        this.showRepository = showRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        cleanOrphanedBookings();
        seedUsers();
        seedMoviesAndShows();
    }

    private void cleanOrphanedBookings() {
        try {
            jdbcTemplate.update("UPDATE bookings b LEFT JOIN users u ON b.user_id = u.user_id SET b.user_id = NULL WHERE b.user_id IS NOT NULL AND u.user_id IS NULL");
        } catch (Exception ignored) {
        }
        try {
            jdbcTemplate.execute("UPDATE shows SET is_active = 1 WHERE is_active IS NULL");
            jdbcTemplate.execute("UPDATE users SET is_active = 1 WHERE is_active IS NULL");
            jdbcTemplate.execute("UPDATE movies SET is_active = 1 WHERE is_active IS NULL");
            jdbcTemplate.execute("UPDATE theatres SET is_active = 1 WHERE is_active IS NULL");
        } catch (Exception ignored) {
        }
    }

    private void seedUsers() {
        User admin = userRepository.findByEmailIgnoreCase("admin@bookmyshow.com").orElseGet(User::new);
        admin.setFullName("BookMyShow Admin");
        admin.setEmail("admin@bookmyshow.com");
        admin.setPassword("admin123");
        admin.setPhone("9876543210");
        admin.setRole("ROLE_ADMIN");
        userRepository.save(admin);

        User admin2 = userRepository.findByEmailIgnoreCase("admin@gmail.com").orElseGet(User::new);
        admin2.setFullName("System Administrator");
        admin2.setEmail("admin@gmail.com");
        admin2.setPassword("admin123");
        admin2.setPhone("9876543211");
        admin2.setRole("ROLE_ADMIN");
        userRepository.save(admin2);

        User user = userRepository.findByEmailIgnoreCase("user@gmail.com").orElseGet(User::new);
        user.setFullName("Rahul Sharma");
        user.setEmail("user@gmail.com");
        user.setPassword("user123");
        user.setPhone("9123456789");
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    private void seedMoviesAndShows() {
        // Fix any existing movies in DB with null or broken poster URLs
        for (Movie m : movieRepository.findAll()) {
            if (m.getPosterUrl() == null || m.getPosterUrl().trim().isEmpty()) {
                m.setPosterUrl("/images/movie-placeholder.jpg");
                movieRepository.save(m);
            }
            if (m.getBannerUrl() == null || m.getBannerUrl().trim().isEmpty()) {
                m.setBannerUrl(m.getPosterUrl());
                movieRepository.save(m);
            }
        }

        if (movieRepository.count() == 0) {

            // Movie 1: Ramayana
            Movie m1 = new Movie();
            m1.setTitle("Ramayana");
            m1.setGenre("Action, Mythology, Drama");
            m1.setLanguage("Hindi, Telugu, Tamil");
            m1.setDuration(180);
            m1.setReleaseDate(LocalDate.of(2026, 10, 24));
            m1.setRating(9.6);
            m1.setVotes("150.8K Votes");
            m1.setDescription("Our Truth. Our History. A magnum opus cinematic retelling of the timeless Indian epic Ramayana starring Ranbir Kapoor as Rama, Sai Pallavi as Sita, and Yash as Ravan.");
            m1.setPosterUrl("/images/ramayana.jpg");
            m1.setBannerUrl("/images/ramayana.jpg");
            m1.setTrailerUrl("https://www.youtube.com/embed/uYPbbksJxIg");
            m1.setCast("Ranbir Kapoor, Sai Pallavi, Yash, Sunny Deol, Ravi Dubey");
            m1.setFormats("2D, 3D, IMAX 3D, 4DX");
            m1.setCertification("U");
            m1 = movieRepository.save(m1);

            // Movie 2: Business Minded
            Movie m2 = new Movie();
            m2.setTitle("Business Minded");
            m2.setGenre("Drama, Thriller");
            m2.setLanguage("Hindi, English");
            m2.setDuration(155);
            m2.setReleaseDate(LocalDate.of(2026, 8, 15));
            m2.setRating(8.7);
            m2.setVotes("42.5K Votes");
            m2.setDescription("Ambition. Power. Success. It's all in the mind. A high-stakes corporate drama uncovering strategies, rivalries, and ambition in modern business.");
            m2.setPosterUrl("/images/business-minded.jpg");
            m2.setBannerUrl("/images/business-minded.jpg");
            m2.setTrailerUrl("https://www.youtube.com/embed/COv52Qyctws");
            m2.setCast("Hrithik Roshan, Siddharth Malhotra, Vicky Kaushal, Taapsee Pannu");
            m2.setFormats("2D, IMAX 2D");
            m2.setCertification("UA");
            m2 = movieRepository.save(m2);

            // Movie 3: Mahayuddh
            Movie m3 = new Movie();
            m3.setTitle("Mahayuddh");
            m3.setGenre("Action, Epic, Drama");
            m3.setLanguage("Telugu, Hindi, Tamil");
            m3.setDuration(153);
            m3.setReleaseDate(LocalDate.of(2026, 9, 10));
            m3.setRating(9.1);
            m3.setVotes("88.3K Votes");
            m3.setDescription("An epic tale of warrior warfare, ancient honor, and destiny starring Allu Arjun in a grand cinematic spectacle directed by Trivikram Srinivas.");
            m3.setPosterUrl("/images/mahayuddh.jpg");
            m3.setBannerUrl("/images/mahayuddh.jpg");
            m3.setTrailerUrl("https://www.youtube.com/embed/d9MyW72ELq0");
            m3.setCast("Allu Arjun");
            m3.setFormats("2D, 3D, IMAX 3D");
            m3.setCertification("UA");
            m3 = movieRepository.save(m3);

            // Movie 4: Master
            Movie m4 = new Movie();
            m4.setTitle("Master");
            m4.setGenre("Action, Thriller");
            m4.setLanguage("Tamil, Hindi, Telugu");
            m4.setDuration(178);
            m4.setReleaseDate(LocalDate.of(2025, 1, 13));
            m4.setRating(8.8);
            m4.setVotes("110.2K Votes");
            m4.setDescription("An alcoholic professor is sent to a juvenile school, where he clashes with a ruthless gangster who uses the children for criminal activities.");
            m4.setPosterUrl("/images/master.jpg");
            m4.setBannerUrl("/images/master.jpg");
            m4.setTrailerUrl("https://www.youtube.com/embed/Way9Dexny3w");
            m4.setCast("Thalapathy Vijay, Vijay Sethupathi, Malavika Mohanan");
            m4.setFormats("2D, IMAX 2D");
            m4.setCertification("UA");
            m4 = movieRepository.save(m4);

            // Movie 5: Dhamaal 4
            Movie m5 = new Movie();
            m5.setTitle("Dhamaal 4");
            m5.setGenre("Comedy, Adventure");
            m5.setLanguage("Hindi");
            m5.setDuration(142);
            m5.setReleaseDate(LocalDate.of(2026, 11, 20));
            m5.setRating(8.3);
            m5.setVotes("35.4K Votes");
            m5.setDescription("The adventure awaits! The comedy squad embarks on another hilarious treasure hunt packed with crazy wild antics and unexpected jungle chaos.");
            m5.setPosterUrl("/images/dhamaal4.jpg");
            m5.setBannerUrl("/images/dhamaal4.jpg");
            m5.setTrailerUrl("https://www.youtube.com/embed/uYPbbksJxIg");
            m5.setCast("Ajay Devgn, Sanjay Dutt, Riteish Deshmukh, Arshad Warsi");
            m5.setFormats("2D, 3D");
            m5.setCertification("U");
            m5 = movieRepository.save(m5);

            // Seed Theatres
            Theatre t1 = new Theatre(null, "PVR IMAX & Directors Cut", "Forum Mall, Koramangala", "Bengaluru", 120);
            Theatre t2 = new Theatre(null, "INOX Megaplex", "Inorbit Mall, Malad", "Mumbai", 150);
            Theatre t3 = new Theatre(null, "Cinepolis VIP", "Select CITYWALK, Saket", "New Delhi", 100);

            t1 = theatreRepository.save(t1);
            t2 = theatreRepository.save(t2);
            t3 = theatreRepository.save(t3);

            // Seed Shows
            LocalDate today = LocalDate.now();
            LocalTime[] times = {
                LocalTime.of(10, 30),
                LocalTime.of(14, 15),
                LocalTime.of(18, 45),
                LocalTime.of(21, 30)
            };

            for (int day = 0; day < 3; day++) {
                LocalDate showDate = today.plusDays(day);

                // Show for Ramayana
                showRepository.save(new Show(null, m1, t1, showDate, times[1], 450.0, t1.getTotalSeats()));
                showRepository.save(new Show(null, m1, t1, showDate, times[3], 500.0, t1.getTotalSeats()));

                // Show for Business Minded
                showRepository.save(new Show(null, m2, t2, showDate, times[0], 300.0, t2.getTotalSeats()));
                showRepository.save(new Show(null, m2, t2, showDate, times[2], 350.0, t2.getTotalSeats()));

                // Show for Mahayuddh
                showRepository.save(new Show(null, m3, t3, showDate, times[1], 380.0, t3.getTotalSeats()));

                // Show for Master
                showRepository.save(new Show(null, m4, t1, showDate, times[2], 280.0, t1.getTotalSeats()));

                // Show for Dhamaal 4
                showRepository.save(new Show(null, m5, t2, showDate, times[3], 250.0, t2.getTotalSeats()));
            }
        }
    }
}
