#include <cstring>
#include <fstream>
#include <iostream>
#include <sstream>
#include <vector>

/**
 * Representation of a single test case.
 */
class TestSuite {
public:
  TestSuite(const std::vector<std::vector<int>>& input,
	    const std::vector<int>& output)
    : input_(input), output_(output) {}
    
  const std::vector<std::vector<int>> GetInput() const {
    return input_;
  }

  /* Print test input to output stream. */
  void PrintInput(std::ostream& stream) const {
    for (const auto& schedule : input_) {
      for (int element : schedule) {
	stream << element << " ";
      }
      stream << std::endl;
    } 
  }

  /* Print formatted test output to output stream. */
  void PrintOutput(std::ostream& stream) const {
    std::vector<std::string> formatted_output = GetFormattedOutput();
    for (const std::string& line : formatted_output) {
      std::cerr << line << std::endl;
    }
  }

  /* Check if output of given stream matches with expected test output. */
  bool Validate(std::istream& stream) const {
    std::vector<std::string> expected = GetFormattedOutput();
    std::vector<std::string> actual;
    std::string line;
    while (std::getline(stream, line)) {
      std::cerr << line << std::endl;
      actual.push_back(line);
    }

    /* Sort to account for threads' asynchronocity. */
    std::sort(expected.begin(), expected.end());
    std::sort(actual.begin(), actual.end());

    if (expected.size() != actual.size()) {
      std::cerr << "Expected: " << expected.size() << " elements." << std::endl;
      std::cerr << "Actual: " << actual.size() << " elements." << std::endl;
      return false;
    }

    for (size_t i = 0; i < expected.size(); ++i) {
      if (expected[i] != actual[i]) {
        std::cerr << "Expected: " << expected[i] << std::endl;
        std::cerr << "Actual: " << actual[i] << std::endl;
        return false;
      }
    }

    return true;
  }

private:
  const std::vector<std::vector<int>> input_;
  const std::vector<int> output_;

  /* Format test output in debug-friendly form. */
  std::vector<std::string> GetFormattedOutput() const {
    std::vector<std::string> output;
    if (output_.empty()) {
      output.push_back("There is no common meeting time.");
    } else {
      for (const int time : output_) {
        output.push_back(std::to_string(time) + " is a common meeting time.");
      }
    }
    return output;
  }
  
};

/* Test cases. */
const TestSuite suites[] =
  {
    {{{5, 3, 2, 4, 1, 5}, {1, 2}, {8, 9, 13, 17, 6, 5, 4, 3, 2}}, {2}},
    {{{1, 1}, {1, 1}, {1, 1}}, {1}},
    {{{4, 1, 2, 3, 4}, {4, 1, 2, 3, 4}, {4, 1, 2, 3, 4}}, {1, 2, 3, 4}},
    {{{3, 1, 2, 3}, {3, 2, 3, 4}, {3, 3, 4, 5}}, {3}},
    {{{1, 1}, {1, 2}, {1, 3}}, {}},
    {{{0}, {0}, {0}}, {}},
    {{{0}, {1, 1}, {1, 1}}, {}},
    {{{1, 1}, {1, 1}, {0}}, {}}
  };
    

int main(int argc, char *argv[]) {
  const int test_number = std::atoi(argv[2]);
  
  if (strcmp(argv[1], "w") == 0) {  /* Write to input file. */
    const std::string filename(argv[3]);
    std::ofstream ofs(filename);
    suites[test_number].PrintInput(ofs);
    ofs.close();
  } else if (strcmp(argv[1], "t") == 0) {  /* Test output from program. */
    std::cerr << "Input #" << test_number << ":" << std::endl;
    suites[test_number].PrintInput(std::cerr);
    std::cerr << "Expected:" << std::endl;
    suites[test_number].PrintOutput(std::cerr);
    std::cerr << "Actual:" << std::endl;
    
    if (suites[test_number].Validate(std::cin)) {
      std::cerr << "Verdict: ACCEPTED" << std::endl;
    } else {
      std::cerr << "Verdict: WRONG" << std::endl;
    }
    std::cerr << "---------------------------------" << std::endl;
  }

  return 0;
}
