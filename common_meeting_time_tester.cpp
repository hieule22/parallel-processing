#include <algorithm>
#include <cstdlib>
#include <cstring>
#include <fstream>
#include <iostream>
#include <sstream>
#include <vector>

class TestSuite {
public:
  TestSuite(const std::vector<std::vector<int>>& input,
	    const std::vector<int>& output)
    : input_(input), output_(output) {}
    
  const std::vector<std::vector<int>> GetInput() const {
    return input_;
  }

  void PrintInput(std::ostream& stream) const {
    for (const auto& schedule : input_) {
      for (int element : schedule) {
	stream << element << " ";
      }
      stream << std::endl;
    } 
  }

  void PrintOutput(std::ostream& stream) const {
    if (output_.empty()) { 
      stream << "There is no common meeting time." << std::endl;
      return;
    }
    for (int time : output_) {
      stream << time << " is a common meeting time." << std::endl;
    }
  }

private:
  const std::vector<std::vector<int>> input_;
  const std::vector<int> output_;
};

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
  // Write data to input file.
  const std::string filename(argv[1]);
  const int test_number = std::atoi(argv[2]);
  std::ofstream ofs(filename);
  suites[test_number].PrintInput(ofs);
  ofs.close();

  // Dump to command terminal.
  std::cout << "Input:" << std::endl;
  suites[test_number].PrintInput(std::cout);
  std::cout << "-----------------------------------" << std::endl;
  std::cout << "Expected output:" << std::endl;
  suites[test_number].PrintOutput(std::cout);
  std::cout << "-----------------------------------" << std::endl;
  std::cout << "Actual output:" << std::endl;

  return 0;
}
