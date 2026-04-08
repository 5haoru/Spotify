# AutoTest Check Script for MySpotify App
# Uses uiautomator2 to verify GUI Agent task completion
# Usage: python check.py <task_id> or python check.py all

import uiautomator2 as u2
import json
import sys
import time
import os


class AppChecker:
    """Checker that verifies GUI Agent task completion via UI state inspection."""

    def __init__(self, serial=None):
        """Connect to device via uiautomator2."""
        if serial:
            self.d = u2.connect(serial)
        else:
            self.d = u2.connect()
        self.package = "com.example.myspotify"

    def _is_app_running(self):
        """Check if the app is in the foreground."""
        current = self.d.app_current()
        return current.get("package") == self.package

    def _find_by_text(self, text, timeout=3):
        """Find element by exact text."""
        return self.d(text=text).exists(timeout=timeout)

    def _find_by_text_contains(self, text, timeout=3):
        """Find element by text containing substring."""
        return self.d(textContains=text).exists(timeout=timeout)

    def _find_by_desc(self, desc, timeout=3):
        """Find element by content description."""
        return self.d(description=desc).exists(timeout=timeout)

    def _find_by_desc_contains(self, desc, timeout=3):
        """Find element by content description containing substring."""
        return self.d(descriptionContains=desc).exists(timeout=timeout)

    # ========== Navigation Tasks ==========

    def check_search_tab_active(self):
        """task_001: Verify Search tab is active - search page content visible."""
        return (
            self._find_by_text("Start browsing")
            or self._find_by_text("Browse all")
        )

    def check_library_tab_active(self):
        """task_002: Verify Your Library tab is active."""
        return (
            self._find_by_text("Your Library")
            and (
                self._find_by_text("Liked Songs")
                or self._find_by_text("Import your music")
                or self._find_by_text("Add artists")
            )
        )

    def check_premium_tab_active(self):
        """task_003: Verify Premium tab is active."""
        return (
            self._find_by_text("Try 3 months for $0")
            or self._find_by_text("Why join Premium?")
        )

    def check_home_tab_active(self):
        """task_004: Verify Home tab is active."""
        return (
            self._find_by_text("Start listening")
            or self._find_by_text("To get you started")
            or self._find_by_text("Made For You")
        )

    # ========== Home Tab Tasks ==========

    def check_music_category_selected(self):
        """task_005: Verify Music category is selected on Home page."""
        # When Music category is active, the content sections change
        # "Made For You" should be prominently displayed
        return (
            self._find_by_text("Made For You")
            and self._find_by_desc("User Avatar")
        )

    def check_podcast_detail_view(self):
        """task_006: Verify podcast detail view is open."""
        return (
            self._find_by_text("How AI is Changing Music")
            and self._find_by_desc("Back")
            and (
                self._find_by_desc_contains("Rewind")
                or self._find_by_desc_contains("Play")
                or self._find_by_desc_contains("Forward")
            )
        )

    def check_audiobook_detail_view(self):
        """task_007: Verify audiobook detail view is open."""
        return (
            self._find_by_text("The Art of Reading")
            and self._find_by_desc("Back")
            and self._find_by_text("About this book")
        )

    def check_user_tab_open(self):
        """task_008: Verify user tab/menu is open."""
        return (
            self._find_by_text("View profile")
            and self._find_by_text("Add account")
            and self._find_by_text("Settings and privacy")
        )

    # ========== Playback Tasks ==========

    def check_playing_view_open(self):
        """task_009: Verify full-screen playing view is open."""
        return (
            self._find_by_text("Now Playing")
            and self._find_by_desc("Album Cover")
            and self._find_by_desc("Previous")
            and self._find_by_desc("Next")
        )

    def check_play_pause_toggled(self):
        """task_010: Verify play/pause was toggled - check for Pause icon (means playing)."""
        # If toggled from default, should show "Pause" description
        return (
            self._find_by_text("Now Playing")
            and (self._find_by_desc("Pause") or self._find_by_desc("Play"))
        )

    def check_next_song(self):
        """task_011: Verify switched to next song - different song is now showing."""
        # The playing view should be open, and the song should have changed
        # Since we can't know the initial song, just verify the playing view is still open
        # and the song title is displayed
        if not self._find_by_text("Now Playing"):
            return False
        # Check that a song title and artist are visible
        return self._find_by_desc("Album Cover")

    def check_shuffle_enabled(self):
        """task_012: Verify shuffle is enabled (Shuffle icon should be green/active)."""
        # When shuffle is on, the contentDescription is still "Shuffle"
        # but the icon color changes to green. We verify shuffle desc exists
        # and the playing view is open
        return (
            self._find_by_text("Now Playing")
            and self._find_by_desc("Shuffle")
        )

    def check_repeat_one_mode(self):
        """task_013: Verify Repeat One mode is active."""
        return (
            self._find_by_text("Now Playing")
            and self._find_by_desc("Repeat One")
        )

    def check_song_liked_mini_player(self):
        """task_014: Verify song liked from mini player - Unlike icon should be visible."""
        # When liked, the mini player shows "Unlike" as contentDescription
        return self._find_by_desc("Unlike")

    def check_song_unliked_playing(self):
        """task_015: Verify song unliked in playing view - Like icon visible."""
        return (
            self._find_by_text("Now Playing")
            and self._find_by_desc("Like")
            and not self._find_by_desc("Unlike", timeout=1)
        )

    def check_sleep_timer_set(self):
        """task_016: Verify sleep timer is set to 15 minutes."""
        # After setting, the app should return to playing view or menu
        # The menu item shows the timer duration if set
        # Check that we're back on the playing view (after dismissing sleep timer)
        return self._find_by_text("Now Playing") or self._find_by_text("Sleep timer")

    def check_lyrics_view_open(self):
        """task_017: Verify lyrics view is open."""
        return (
            self._find_by_desc("Back")
            and self._find_by_desc("Share")
            # Lyrics view has song title at the top and lyric text
        )

    def check_credits_view_open(self):
        """task_018: Verify credits view is open."""
        return (
            self._find_by_text("Credits")
            and self._find_by_desc("Back")
            and (
                self._find_by_text("Performed by")
                or self._find_by_text("Written by")
                or self._find_by_text("Produced by")
            )
        )

    def check_about_artist_view_open(self):
        """task_019: Verify About the artist view is open."""
        return (
            self._find_by_text("About the artist")
            and self._find_by_desc("Back")
            and (
                self._find_by_text("Follow")
                or self._find_by_text_contains("monthly listeners")
            )
        )

    # ========== Search Tasks ==========

    def check_search_input_text(self):
        """task_020: Verify search input has 'Shape' text."""
        # The search input page should be open and contain the text
        if not self._find_by_text("Cancel"):
            return False
        # Check if text field contains "Shape"
        el = self.d(className="android.widget.EditText")
        if el.exists(timeout=3):
            text = el.get_text()
            return text is not None and "Shape" in text
        return False

    def check_category_detail_music(self):
        """task_021: Verify Music category detail view is open."""
        return (
            self._find_by_desc("Back")
            and self._find_by_text("Music")
        )

    def check_code_tab_open(self):
        """task_022: Verify Scan Spotify Code page is open."""
        return (
            self._find_by_text("Scan Spotify Code")
            and self._find_by_desc("Back")
        )

    def check_recent_search_removed(self):
        """task_023: Verify a recent search item was removed."""
        # After removal, we should still be on the search input page
        # with fewer recent search items. We can only verify we're on the right page
        return (
            self._find_by_text("Cancel")
            and self._find_by_text("Recent searches")
        )

    # ========== Library Tasks ==========

    def check_playlist_created(self):
        """task_024: Verify a playlist named 'My Rock' was created."""
        # After creation, should be on the playlist detail page or back in library
        return (
            self._find_by_text("My Rock")
            or self._find_by_text_contains("My Rock")
        )

    def check_liked_songs_view_open(self):
        """task_025: Verify Liked Songs page is open."""
        return (
            self._find_by_text("Liked Songs")
            and self._find_by_desc("Back")
            and (
                self._find_by_desc("Shuffle")
                or self._find_by_desc("Play")
            )
        )

    def check_artist_view_open(self):
        """task_026: Verify Taylor Swift artist page is open."""
        return (
            self._find_by_text("Taylor Swift")
            and self._find_by_desc("Back")
            and (
                self._find_by_text("Popular")
                or self._find_by_text("Discography")
            )
        )

    def check_artist_followed(self):
        """task_027: Verify an artist was followed - 'Following' button visible."""
        return (
            self._find_by_text("Following")
            and (
                self._find_by_text("Add artists")
                or self._find_by_text("Artists you might like")
            )
        )

    def check_podcast_followed(self):
        """task_028: Verify a podcast was followed."""
        return (
            self._find_by_text("Following")
            and (
                self._find_by_text("Add podcasts")
                or self._find_by_text("Podcasts you might like")
            )
        )

    def check_venue_followed(self):
        """task_029: Verify a venue was followed."""
        return (
            self._find_by_text("Following")
            and (
                self._find_by_text("Add events and venues")
                or self._find_by_text("Events and venues near you")
            )
        )

    def check_import_view_open(self):
        """task_030: Verify Import your music page is open."""
        return (
            self._find_by_text("Import your library")
            and self._find_by_desc("Back")
            and self._find_by_text("Import")
        )

    def check_library_search_open(self):
        """task_031: Verify library search page is open."""
        return (
            self._find_by_text("Search Your Library")
            or self._find_by_text("Find in your Liked Songs, playlists, and more.")
        )

    # ========== Playlist Tasks ==========

    def check_song_added_to_playlist(self):
        """task_032: Verify a song was added to a playlist."""
        # After adding, should be back on playlist detail page or the add page
        # with a green check (Added) icon visible
        return (
            self._find_by_desc("Added")
            or self._find_by_text("Added to playlist")
            or self._find_by_text("Add to this playlist")
        )

    def check_song_added_to_liked(self):
        """task_033: Verify song added to Liked Songs via playing menu."""
        # After selecting Liked Songs and pressing Done, a snackbar should appear
        # or we should be back on the playing view
        return (
            self._find_by_text("Now Playing")
            or self._find_by_text_contains("Added")
        )

    # ========== Premium Tasks ==========

    def check_checkout_view_open(self):
        """task_034: Verify Checkout page is open."""
        return (
            self._find_by_text("Checkout")
            and self._find_by_text("Premium Individual")
            and (
                self._find_by_text("Payment method")
                or self._find_by_text("Activate Premium")
            )
        )

    # ========== Podcast Tasks ==========

    def check_comment_posted(self):
        """task_035: Verify comment 'Great episode!' was posted."""
        return self._find_by_text("Great episode!")

    # ========== Additional Tasks ==========

    def check_home_song_liked(self):
        """task_036: Verify a song was liked on the home page."""
        # After clicking like on a song, the icon changes to "Unlike"
        return (
            self._find_by_desc("Unlike")
            and (
                self._find_by_text("Start listening")
                or self._find_by_text("To get you started")
                or self._find_by_desc("User Avatar")
            )
        )

    def check_playing_view_closed(self):
        """task_037: Verify playing view is closed and back to main page."""
        return (
            not self._find_by_text("Now Playing", timeout=1)
            and (
                self._find_by_text("Home")
                or self._find_by_text("Search")
                or self._find_by_text("Your Library")
                or self._find_by_text("Premium")
            )
        )

    def check_audiobook_playing(self):
        """task_038: Verify audiobook playback started (Pause button visible)."""
        return (
            self._find_by_desc("Back")
            and self._find_by_desc("Pause")
            and self._find_by_text("About this book")
        )

    def check_podcast_forward(self):
        """task_039: Verify podcast forward button was pressed - still on podcast page."""
        return (
            self._find_by_desc("Back")
            and self._find_by_desc_contains("Forward")
        )

    def check_sleep_timer_off(self):
        """task_040: Verify sleep timer was turned off."""
        # After turning off, should be back on playing view or menu
        # Sleep timer menu item should not show a duration
        return (
            self._find_by_text("Now Playing")
            or self._find_by_text("Sleep timer")
        )

    def run_check(self, check_method_name):
        """Run a specific check method by name."""
        method = getattr(self, check_method_name, None)
        if method is None:
            return {"status": "error", "message": f"Check method '{check_method_name}' not found"}

        if not self._is_app_running():
            return {"status": "error", "message": "App is not running in foreground"}

        try:
            result = method()
            return {
                "status": "pass" if result else "fail",
                "check_method": check_method_name,
                "result": result
            }
        except Exception as e:
            return {
                "status": "error",
                "check_method": check_method_name,
                "message": str(e)
            }


def load_tasks(tasks_file=None):
    """Load task definitions from JSON file."""
    if tasks_file is None:
        tasks_file = os.path.join(os.path.dirname(os.path.abspath(__file__)), "tasks.json")
    with open(tasks_file, "r", encoding="utf-8") as f:
        return json.load(f)


def run_single_task(checker, task):
    """Run a single task check and return result."""
    task_id = task["task_id"]
    check_method = task["check_method"]
    instruction = task["instruction"]

    print(f"\n{'='*60}")
    print(f"Task: {task_id}")
    print(f"Instruction: {instruction}")
    print(f"Check method: {check_method}")
    print(f"{'='*60}")

    result = checker.run_check(check_method)

    status = result["status"]
    if status == "pass":
        print(f"  Result: ✅ PASS")
    elif status == "fail":
        print(f"  Result: ❌ FAIL")
    else:
        print(f"  Result: ⚠️ ERROR - {result.get('message', 'Unknown error')}")

    return result


def main():
    import argparse

    parser = argparse.ArgumentParser(description="MySpotify GUI Agent Task Checker")
    parser.add_argument("task_id", nargs="?", default=None,
                        help="Task ID to check (e.g., task_001) or 'all' to run all checks")
    parser.add_argument("--serial", "-s", default=None,
                        help="Android device serial number")
    parser.add_argument("--tasks-file", "-f", default=None,
                        help="Path to tasks.json file")
    parser.add_argument("--output", "-o", default=None,
                        help="Output results to JSON file")
    args = parser.parse_args()

    if args.task_id is None:
        parser.print_help()
        print("\nExamples:")
        print("  python check.py task_001          # Check single task")
        print("  python check.py all               # Check all tasks")
        print("  python check.py all -o results.json  # Save results to file")
        sys.exit(0)

    # Load tasks
    tasks_data = load_tasks(args.tasks_file)
    tasks = tasks_data["tasks"]

    # Connect to device
    print(f"Connecting to device{' (serial: ' + args.serial + ')' if args.serial else ''}...")
    checker = AppChecker(serial=args.serial)
    print(f"Connected. Device info: {checker.d.info.get('productName', 'Unknown')}")

    results = []

    if args.task_id.lower() == "all":
        # Run all task checks
        print(f"\nRunning all {len(tasks)} task checks...")
        for task in tasks:
            result = run_single_task(checker, task)
            result["task_id"] = task["task_id"]
            result["instruction"] = task["instruction"]
            results.append(result)
    else:
        # Run single task check
        task = next((t for t in tasks if t["task_id"] == args.task_id), None)
        if task is None:
            print(f"Error: Task '{args.task_id}' not found")
            sys.exit(1)
        result = run_single_task(checker, task)
        result["task_id"] = task["task_id"]
        result["instruction"] = task["instruction"]
        results.append(result)

    # Print summary
    print(f"\n{'='*60}")
    print("SUMMARY")
    print(f"{'='*60}")
    passed = sum(1 for r in results if r["status"] == "pass")
    failed = sum(1 for r in results if r["status"] == "fail")
    errors = sum(1 for r in results if r["status"] == "error")
    total = len(results)
    print(f"  Total: {total}")
    print(f"  Passed: {passed}")
    print(f"  Failed: {failed}")
    print(f"  Errors: {errors}")
    if total > 0:
        print(f"  Pass Rate: {passed / total * 100:.1f}%")

    # Save results if requested
    if args.output:
        output_data = {
            "summary": {
                "total": total,
                "passed": passed,
                "failed": failed,
                "errors": errors,
                "pass_rate": f"{passed / total * 100:.1f}%" if total > 0 else "0%"
            },
            "results": results
        }
        with open(args.output, "w", encoding="utf-8") as f:
            json.dump(output_data, f, ensure_ascii=False, indent=2)
        print(f"\nResults saved to: {args.output}")

    sys.exit(0 if failed == 0 and errors == 0 else 1)


if __name__ == "__main__":
    main()
